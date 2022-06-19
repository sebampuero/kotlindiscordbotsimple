package chistosito.event

import chistosito.command.*
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.CoroutineScope

internal class MessageArrivedEvent(private val event: MessageCreateEvent) : DiscordEvent() {

    override suspend fun doSomething(scope: CoroutineScope) {
        // ignore other bots, even ourselves. We only serve humans here!
        if (event.message.author?.isBot != false) return
        PingCommand().execute(event)
        LolCommand().execute(event)
    }

}