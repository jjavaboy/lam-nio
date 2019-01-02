package lam.kotlin.study

fun main(args: Array<String>) {
    val f: (Int) -> Boolean = {
        it > 0
    }
    // true
    println(foo(f));

    val p: Predicate<Int> = {
        it > 0
    }
    val list = listOf(9, -8, 7, -6, 5, -4, 3, -2, 1)
    // [9, 7, 5, 3, 1]
    println(list.filter(p))
}

fun foo(p: Predicate<Int>) = p(42)

typealias NodeSet = Set<String>
// or
typealias MyHandler    = (Int, String, Any) -> Unit
typealias Predicate<T> = (T) -> Boolean

// or
typealias AAInner = AA.Inner
class AA {
    inner class Inner
}

