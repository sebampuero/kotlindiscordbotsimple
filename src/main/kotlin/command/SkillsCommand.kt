package chistosito.command

import chistosito.parser
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

class SkillsCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "skills")) return
        val msgContent = msgEvent.message.content
        val inputChamp = msgContent.split(" ").filter { it != "!items" }.joinToString(" ")
        parser(inputChamp, "https://rankedboost.com/league-of-legends/build/$inputChamp") {
            msgEvent.message.channel.createMessage(text("skill-order", "rb-build-sec-desc"))
        }
    }
}