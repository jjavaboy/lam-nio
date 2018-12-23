package lam.kotlin.study

fun main(args: Array<String>) {
    for (i in 1..10) { println(i); }

    for (i in 1..10 step 2) { println(i);}

    for (i in 10 downTo 1) { println(i); }

    for (i in 10 downTo 1 step 4) { println(i); }
}