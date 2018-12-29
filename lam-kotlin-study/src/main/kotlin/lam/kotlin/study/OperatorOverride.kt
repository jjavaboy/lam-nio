package lam.kotlin.study

// https://www.kotlincn.net/docs/reference/operator-overloading.html

class OperatorOverride (val x: Int, val y: Int)

operator fun OperatorOverride.plus(o: OperatorOverride) {
    println("${o.x + this.x}, ${o.y + this.y}")
}

operator fun OperatorOverride.plus(v: Int): Int {
    return this.x + this.y + v
}

fun main(args: Array<String>) {

    OperatorOverride(1, 2) + OperatorOverride(3, 4)

    println(OperatorOverride(1, 2) + 3)
}