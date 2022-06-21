package chistosito.command

import chistosito.parser
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

class RunesCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "runes")) return
        val msgContent = msgEvent.message.content
        val inputChamp = msgContent.split(" ").filter { it != "!items" }.joinToString(" ")
        parser(inputChamp, "https://rankedboost.com/league-of-legends/build/$inputChamp") {
            msgEvent.message.channel.createMessage(text("runes", "text-setion-lol-build-area"))
        }
    }
}