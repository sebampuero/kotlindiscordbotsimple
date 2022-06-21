package chistosito.command

import chistosito.parser
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

class ItemsCommand: LolCommand() {
    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "items")) return
        val msgContent = msgEvent.message.content
        val inputChamp = msgContent.split(" ").filter { it != "!items" }.joinToString(" ")
        parser(inputChamp, "https://rankedboost.com/league-of-legends/build/$inputChamp") {
            msgEvent.message.channel.createMessage(text("item-build", "rb-build-sec-desc"))
            val setOfImages = images("item-build", "rb-item-img").toSet()
            //Step1: download locally (better if stored in memory)
            //Step2: upload them all as a message and send
            for(img in setOfImages){
                msgEvent.message.channel.createMessage(img)
            }
        }
    }
}