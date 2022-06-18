package chistosito.command

import dev.kord.core.event.Event

abstract class Command {

    abstract suspend fun execute(event: Event)

}