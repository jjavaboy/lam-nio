package lam.kotlin.study

class MyDelegation(map: MutableMap<String, Any>) {
    var name: String by map
    var gender: Int by map
}

fun main(args: Array<String>) {
    var map = mutableMapOf("name" to "LAM", "gender" to 1)
    var my  = MyDelegation(map)
    println("name: ${my.name}, gender: ${my.gender}")
}