package chistosito.command

import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.CoroutineScope

class PingCommand: Command() {

    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(msgEvent.message.content != "!ping") return
        msgEvent.message.channel.createMessage("pong!")
    }


}