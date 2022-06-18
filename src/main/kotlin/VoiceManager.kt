package chistosito

import chistosito.media.AudioMedia
import chistosito.media.LocalFileAudioMedia
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.VoiceState
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object VoiceManager {

    private val connections: MutableMap<Snowflake, VoiceConnection> = mutableMapOf()
    private val audioChannel = Channel<Pair<AudioMedia, VoiceState>>()

    private suspend fun greetMemberEnteredVoiceChannel(state: VoiceState) {
        val userId = state.getMember().id
        state.getChannelOrNull()
        for(voiceMember in VoiceMembers.values()){
            if(userId.toString() == voiceMember.userId){
                // process user key (to lower case)
                val userKey = voiceMember.name.lowercase()
                // list objects for user
                val availableAudios = AWSWorker.listObjectsInBucket("discordbot/audio_assets/", "sebampuerombucket",
                    userKey
                )
                // pick random
                val audioFile = availableAudios.random()
                // download
                val audioPathFile = AWSWorker.s3ObjectDownload(audioFile, "sebampuerombucket", "./$userKey.mp3")
                // add to queue
                audioChannel.send(Pair(LocalFileAudioMedia(audioPathFile), state))
                // consumer of queue knows and start reproducing
            }
        }
    }

    suspend fun onStateUpdateEvent(old: VoiceState?, state: VoiceState) {
        if (old?.channelId == null) {
            greetMemberEnteredVoiceChannel(state)
        }
    }

    fun startAudioConsumer(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            while(true){
                val audioAndVoiceChannel = audioChannel.receive()
                val state = audioAndVoiceChannel.second
                val voiceChannel = state.getChannelOrNull()
                val lavaPlayerManager = DefaultAudioPlayerManager()
                AudioSourceManagers.registerRemoteSources(lavaPlayerManager)
                val player = lavaPlayerManager.createPlayer()
                val track = lavaPlayerManager.playTrack(audioAndVoiceChannel.first.resource, player)
                //voiceChannel?.connect {
//
//                } TODO: wait for fix https://github.com/kordlib/kord/issues/613 or try to fix it myself later
            }
        }
    }

}

suspend fun DefaultAudioPlayerManager.playTrack(query: String, player: AudioPlayer): AudioTrack? {
    val track = suspendCoroutine<AudioTrack?> {
        this.loadItem(query, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                println("track loaded")
                it.resume(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                it.resume(playlist.tracks.first())
            }

            override fun noMatches() {
                println("No matches")
                it.resume(null)
            }

            override fun loadFailed(exception: FriendlyException?) {
                println("Load failed")
                it.resume(null)
            }
        })
    }

    player.playTrack(track)

    return track
}