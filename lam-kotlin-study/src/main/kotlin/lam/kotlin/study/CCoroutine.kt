package lam.kotlin.study

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {

    var job = GlobalScope.launch {
        delay(1000L)
        println("Kotlin")
    }
    println("Hello")
    job.join()

    coroutineScope {
        launch {
            delay(50L)
            println("Task from nested launch")
        }
        delay(100)
        println("Task from coroutine scope")
    }

    println("Coroutine scope is over")

    GlobalScope.launch {
        doWork()
    }
    println("lalala")

    var job2 = GlobalScope.launch {
        repeat(10) {
            i ->
            println("i:" + i + ", lalala")
        }
    }
    job2.join()
    println("finished")
}

suspend fun doWork() {
    delay(10L)
    println("doWork")
}