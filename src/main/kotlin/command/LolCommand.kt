package chistosito.command

import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent

open class LolCommand: Command() {
    override suspend fun execute(event: Event) {
        LoLEnemyTipsCommand().execute(event)
        LolAllyTipsCommand().execute(event)
        LolRunesCommand().execute(event)
        LolSkillsCommand().execute(event)
        LolItemsCommand().execute(event)
    }
}

suspend fun validSyntax(msgEvent: MessageCreateEvent, command: String): Boolean {
    val msgContent = msgEvent.message.content
    if(!msgContent.contains(command)) return false
    if(msgContent.split(" ").size == 1) {
        msgEvent.message.channel.createMessage("Syntax: !$command <champ>")
        return false
    }
    return true
}

fun extractChamp(inputStr: String, command: String): String {
    return inputStr.split(" ").filter { it != command }.joinToString(" ")
}