package lam.kotlin.study

fun main(args: Array<String>) {
    val a = "a"
    val b: String? = null

    // print 1
    println(a?.length)

    // print null
    println(b?.length)

    // print 1
    println(a?.length ?: 0)

    // print 0
    println(b?.length ?: 0)

    // throw NPE: Exception in thread "main" kotlin.KotlinNullPointerException
    println(b!!.length)

    val list = mutableListOf(1, 2, null, 3);
    // [1, 2, null, 3]
    println(list)

    val l = list.filterNotNull()
    // [1, 2, 3]
    println(l)
}