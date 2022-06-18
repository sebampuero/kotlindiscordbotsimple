package chistosito

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {

    while(true) {
        var mainJob: Job? = null
        try {
            mainJob = DiscordBot(scope = this).mainJob
            mainJob?.join()
        }catch (exception: Exception) {
            println("$exception and rejoining")
            mainJob?.cancelAndJoin()
        }finally {
            delay(1000)
        }
    }
}