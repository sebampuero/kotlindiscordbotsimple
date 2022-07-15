package chistosito.voice

import chistosito.AWSWorker
import chistosito.VoiceMembers
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.connect
import dev.kord.core.entity.VoiceState
import dev.kord.voice.AudioFrame
import dev.kord.voice.VoiceConnection

object VoiceManager {

    private val connections: MutableMap<Snowflake, VoiceConnection?> = mutableMapOf()
    private val lavaPlayerManager: DefaultAudioPlayerManager = DefaultAudioPlayerManager()
    private val player: AudioPlayer
    private val trackScheduler: TrackScheduler

    init {
        AudioSourceManagers.registerLocalSource(lavaPlayerManager)
        player = lavaPlayerManager.createPlayer()
        trackScheduler = TrackScheduler(player, connections)
        player.addListener(trackScheduler)
    }

    private suspend fun greetMemberEnteredVoiceChannel(state: VoiceState) {
        val userId = state.getMember().id
        for(voiceMember in VoiceMembers.values()){
            if(userId.toString() == voiceMember.userId){
                // process user key (to lower case)
                val userKey = voiceMember.name.lowercase()
                // list objects for user
                val availableAudios = AWSWorker.listObjectsInBucket(
                    "discordbot/audio_assets/", "sebampuerombucket",
                    userKey
                )
                // pick random
                val audioFile = availableAudios.random()
                // download
                val audioPathFile = AWSWorker.s3ObjectDownload(audioFile, "sebampuerombucket", "$userKey.mp3")
                lavaPlayerManager.loadItem(audioPathFile, object: AudioLoadResultHandler{
                    override fun trackLoaded(track: AudioTrack) {
                        trackScheduler.queue(track)
                        trackScheduler.startPlaying()
                    }

                    override fun playlistLoaded(playlist: AudioPlaylist?) {
                        if (playlist != null) {
                            trackScheduler.queue(playlist.tracks.first())
                        }
                    }

                    override fun noMatches() {
                    }

                    override fun loadFailed(exception: FriendlyException?) {
                    }

                })
                connectToVoiceChannel(state.guildId, state)
            }
        }
    }

    private suspend fun connectToVoiceChannel(guildId: Snowflake, state: VoiceState) {
        if(connections[guildId!!] == null){
            val voiceChannel = state.getChannelOrNull()
            val connection = voiceChannel?.connect {
                audioProvider { AudioFrame.fromData(player.provide()?.data) }
            }
            connections[guildId!!] = connection
            trackScheduler.currConnection = guildId
        }
    }

    suspend fun onStateUpdateEvent(old: VoiceState?, newState: VoiceState) {
        if (old?.channelId == null || (newState.channelId != old.channelId && newState.channelId != null)) {
            greetMemberEnteredVoiceChannel(newState)
        }
    }

}