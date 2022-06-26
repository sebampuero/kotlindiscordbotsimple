package chistosito.command

import chistosito.parser
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

class RunesCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "runes")) return
        val msgContent = msgEvent.message.content
        val inputChamp = extractChamp(msgContent, "!runes")
        parser("https://rankedboost.com/league-of-legends/build/$inputChamp") {
            firstFound("runes", "text-setion-lol-build-area")?.let { msgEvent.message.channel.createMessage(it.text()) }
            val runesElements = elementsByClass("runes", "rb-build-rune-text")
            val sb = java.lang.StringBuilder()
            for((index, rune) in runesElements.withIndex()){
                sb.apply {
                    append(rune.text())
                    if(index != runesElements.size - 1) append(",")
                }
            }
            msgEvent.message.channel.createMessage(sb.toString())
        }
    }
}