package lam.kotlin.study

import java.lang.IllegalArgumentException

fun main(args: Array<String>) {
    fail("test exception")

    // return type of function fail() is `Nothing`, it's unreachable code below:
    println("aaabbb")
}

fun fail(message: String): Nothing {
    throw IllegalArgumentException(message)
}