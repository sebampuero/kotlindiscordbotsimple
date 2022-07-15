package chistosito.event

import chistosito.voice.VoiceManager
import dev.kord.core.event.user.VoiceStateUpdateEvent
import kotlinx.coroutines.CoroutineScope

class VoiceStateUpdateEvent(private val event: VoiceStateUpdateEvent) : DiscordEvent() {

    override suspend fun doSomething(scope: CoroutineScope) {
        VoiceManager.onStateUpdateEvent(event.old, event.state)
    }
}