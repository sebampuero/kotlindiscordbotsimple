package chistosito.command

import chistosito.parser
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

class LolItemsCommand: LolCommand() {

    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "items")) return
        val msgContent = msgEvent.message.content
        val inputChamp = extractChamp(msgContent, "!items")
        parser("https://rankedboost.com/league-of-legends/build/$inputChamp") {
            msgEvent.message.channel.createMessage {
                attributesInClass("item-build", "rb-build-off-item-imgs", "img", "src").forEachIndexed { i, img ->
                    val url = URL(img)
                    url.openStream().use {
                        Files.copy(it, Paths.get("$i.png"))
                        addFile(Paths.get("$i.png"))
                        Files.delete(Paths.get("$i.png"))
                    }
                }
            }
            firstFound("item-build", "rb-build-sec-desc")?.let { msgEvent.message.channel.createMessage(it.text()) }
        }
    }
}