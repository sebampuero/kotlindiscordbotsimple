package chistosito.command

import chistosito.parser
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

class RunesCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "runes")) return
        val msgContent = msgEvent.message.content
        val inputChamp = msgContent.split(" ").first { it != "!runes" }
        parser(inputChamp) {
            msgEvent.message.channel.createMessage(text("runes", "text-setion-lol-build-area"))
        }
    }
}