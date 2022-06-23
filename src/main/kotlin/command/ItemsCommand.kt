package chistosito.command

import chistosito.parser
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class ItemsCommand: LolCommand() {

    val listOfSummSpells = listOf("Flash", "Heal", "Teleport", "Exhaust", "Ghost",
        "Barrier", "Mark", "Dash", "Clarity", "Smite", "Cleanse", "Ignite")

    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "items")) return
        val msgContent = msgEvent.message.content
        val inputChamp = extractChamp(msgContent, "!items")
        parser("https://rankedboost.com/league-of-legends/build/$inputChamp") {
            val listOfImages = images("item-build", "rb-item-img").toSet()
            val listOfImagesWithoutSpells = listOfImages.toMutableSet()
            listOfSummSpells.forEach { spell ->
                listOfImages.forEach { img ->
                    if(img.contains(spell)) listOfImagesWithoutSpells.remove(img)
                }
            }
            msgEvent.message.channel.createMessage {
                for((index, img) in listOfImagesWithoutSpells.withIndex()){
                    val url = URL(img)
                    url.openStream().use {
                        Files.copy(it, Paths.get("$index.png"))
                        addFile(Paths.get("$index.png"))
                        Files.delete(Paths.get("$index.png"))
                    }
                }
            }
            msgEvent.message.channel.createMessage(text("item-build", "rb-build-sec-desc"))
        }
    }
}