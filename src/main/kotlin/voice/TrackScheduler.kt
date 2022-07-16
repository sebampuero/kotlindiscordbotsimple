package chistosito.voice

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.kord.common.entity.Snowflake
import dev.kord.voice.VoiceConnection
import kotlinx.coroutines.runBlocking
import java.util.*

/**
 * Maintains the current connection and a queue of tracks that will be played by the AudioPlayer. When no more tracks
 * are in the queue the connection is terminated.
 */
class TrackScheduler(private val realPlayer: AudioPlayer, private val connections: MutableMap<Snowflake, VoiceConnection?> ) : AudioEventAdapter() {

    private val audioQueue: Queue<AudioTrack> = LinkedList()
    var currConnection: Snowflake? = null
    private var isPlaying = false
    private var currTrackPlaying = ""

    fun queue(track: AudioTrack) {
        for(aTrack in audioQueue){
            if(aTrack.identifier == track.identifier) return
        }
        if(currTrackPlaying != track.identifier) audioQueue.add(track)
    }

    fun startPlaying() {
        if(!isPlaying){
            val track = audioQueue.remove()
            realPlayer.playTrack(track)
            currTrackPlaying = track.identifier
        }
        isPlaying = true
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        if(!audioQueue.isEmpty()) {
            val audioTrack = audioQueue.remove()
            realPlayer.playTrack(audioTrack)
            currTrackPlaying = audioTrack.identifier
        }else{
            isPlaying = false
            currTrackPlaying = ""
            runBlocking {
                connections.remove(currConnection)?.shutdown()
            }
        }
    }
}