package chistosito.command

import dev.kord.core.event.Event
import kotlinx.coroutines.CoroutineScope

abstract class Command {

    abstract suspend fun execute(event: Event)

}