package chistosito.event

import kotlinx.coroutines.CoroutineScope

abstract class DiscordEvent {

    abstract suspend fun doSomething(scope: CoroutineScope)

}