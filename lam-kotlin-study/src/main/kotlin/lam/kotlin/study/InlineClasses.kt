package lam.kotlin.study

inline class Named(val name: String)

fun main(args: Array<String>) {

    val name = Named("LAM")
    println(name.toString())

}
