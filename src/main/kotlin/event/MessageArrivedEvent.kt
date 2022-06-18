package chistosito.event

import chistosito.command.LoLEnemyTipsCommand
import chistosito.command.LolAllyTipsCommand
import chistosito.command.PingCommand
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.CoroutineScope

internal class MessageArrivedEvent(private val event: MessageCreateEvent) : DiscordEvent() {

    override suspend fun doSomething(scope: CoroutineScope) {
        // ignore other bots, even ourselves. We only serve humans here!
        if (event.message.author?.isBot != false) return
        PingCommand().execute(event)
        LoLEnemyTipsCommand().execute(event)
        LolAllyTipsCommand().execute(event)
    }

}