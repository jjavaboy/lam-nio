package lam.kotlin.study

import kotlin.reflect.KClass

fun main(args: Array<String>) {
    val kc = String::class
    // true
    println(kc is KClass)

    val v = String::class.java
    // String in java
    println(v)

    println(isEven(2))

    val numbers = listOf(1, 2, 3, 4)
    // filter number(which is even) to new list object
    println(numbers.filter(::isEven))

    // 1
    println(::x.get())

    // x
    println(::x.name)

    ::x.set(2)
    // 2
    println(::x.get())
}
var x = 1

fun isEven(x: Int) = x % 2 == 0