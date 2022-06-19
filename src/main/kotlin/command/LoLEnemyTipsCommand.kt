package chistosito.command

import chistosito.model.ChampModel
import com.google.gson.Gson
import dev.kord.core.event.Event
import dev.kord.core.event.message.MessageCreateEvent
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class LoLEnemyTipsCommand : LolCommand() {

    override suspend fun execute(event: Event) {
        val msgEvent = event as MessageCreateEvent
        if(!validSyntax(msgEvent, "enemytips")) return
        val msgContent = msgEvent.message.content
        val client = HttpClient(CIO)
        var response: HttpResponse = client.get("https://ddragon.leagueoflegends.com/api/versions.json")
        val versionsStr: String = response.body()
        val lastVersion = versionsStr
            .replace("[", "")
            .replace("]", "")
            .replace("\"", "")
            .split(",")
            .first()
        val inputChamp = msgContent.split(" ").
            filter { it != "!enemytips" }.
            map { it.first().uppercase() + it.subSequence(1, it.length) }.
            joinToString("")
        response = client.get("http://localhost:6000/champ?champ=$inputChamp&version=$lastVersion")
        if(response.status.value != 200) {
            msgEvent.message.channel.createMessage("Escribe bien")
            return
        }
        val champJson: String = response.body()
        val champ = Gson().fromJson(champJson, ChampModel::class.java)
        champ.enemytips.forEach {
            msgEvent.message.channel.createMessage(it)
        }
        if(champ.enemytips.size == 0) {
            msgEvent.message.channel.createMessage("No hay tips pa $inputChamp")
        }
    }
}