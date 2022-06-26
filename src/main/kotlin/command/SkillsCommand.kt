package chistosito.command

import chistosito.parser
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.CoroutineScope

class SkillsCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "skills")) return
        val msgContent = msgEvent.message.content
        val inputChamp = extractChamp(msgContent, "!skills")
        parser("https://rankedboost.com/league-of-legends/build/$inputChamp") {
            tableAsText("rb-build-skill-seq").forEach {
                val sb = java.lang.StringBuilder()
                it.forEach {
                    sb.apply {
                        append(it)
                        append(" ")
                    }
                }
                msgEvent.message.channel.createMessage(sb.toString())
            }
            firstFound("skill-order", "rb-build-sec-desc")?.let { msgEvent.message.channel.createMessage(it.text()) }
        }
    }
}