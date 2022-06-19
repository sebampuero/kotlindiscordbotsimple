package chistosito.command

import chistosito.parser
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

class ItemsCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "items")) return
        val msgContent = msgEvent.message.content
        val inputChamp = msgContent.split(" ").first { it != "!items" }
        parser(inputChamp) {
            msgEvent.message.channel.createMessage(text("item-build", "rb-build-sec-desc"))
        }
    }
}