package chistosito.command

import chistosito.parser
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import kotlinx.coroutines.CoroutineScope

class RunesCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "runes")) return
        val msgContent = msgEvent.message.content
        val inputChamp = extractChamp(msgContent, "!runes")
        parser("https://rankedboost.com/league-of-legends/build/$inputChamp") {
            msgEvent.message.channel.createMessage(text("runes", "text-setion-lol-build-area"))
            val runesElements = setOfClassElements("runes", "rb-build-rune-text")
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