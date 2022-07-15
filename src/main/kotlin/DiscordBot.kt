package chistosito

import chistosito.event.MessageArrivedEvent
import dev.kord.core.Kord
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.event.user.VoiceStateUpdateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import kotlinx.coroutines.*

class DiscordBot (scope: CoroutineScope) {

    var mainJob: Job? = null
    private var kord: Kord? = null

    init {
        mainJob = scope.launch {
            val discordBotToken = System.getenv("BOT_TOKEN")
            assert(discordBotToken != null)
            kord = Kord(discordBotToken) //get from env variable
            initEvents(this)
            kord?.login {
                // we need to specify this to receive the content of messages
                @OptIn(PrivilegedIntent::class)
                intents += Intent.MessageContent
            }
        }
    }

    private suspend fun initEvents(scope: CoroutineScope) {
        kord?.on<MessageCreateEvent> {
            MessageArrivedEvent(this).doSomething(scope)
        }
        kord?.on<VoiceStateUpdateEvent> {
            chistosito.event.VoiceStateUpdateEvent(this).doSomething(scope)
        }
    }

    suspend fun killBot() {
        mainJob?.cancelAndJoin()
    }

}