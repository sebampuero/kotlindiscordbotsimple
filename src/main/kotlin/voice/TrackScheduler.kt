package chistosito.voice

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.kord.common.entity.Snowflake
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.LinkedHashSet

class TrackScheduler(private val realPlayer: AudioPlayer, private val connections: MutableMap<Snowflake, VoiceConnection?> ) : AudioEventAdapter() {

    private val audioQueue: Queue<AudioTrack> = LinkedList()
    var currConnection: Snowflake? = null
    private var isPlaying = false
    private var currentTrackPlaying = ""

    fun queue(track: AudioTrack) {
        for(aTrack in audioQueue){
            if(aTrack.identifier == track.identifier) return
        }
        if(currentTrackPlaying != track.identifier) audioQueue.add(track)
    }

    fun startPlaying() {
        if(!isPlaying){
            val track = audioQueue.remove()
            realPlayer.playTrack(track)
            currentTrackPlaying = track.identifier
        }
        isPlaying = true
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        if(!audioQueue.isEmpty()) {
            val audioTrack = audioQueue.remove()
            realPlayer.playTrack(audioTrack)
            currentTrackPlaying = audioTrack.identifier
        }else{
            isPlaying = false
            currentTrackPlaying = ""
            runBlocking {
                connections.remove(currConnection)?.shutdown()
            }
        }
    }
}