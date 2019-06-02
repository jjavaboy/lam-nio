package lam.kotlin.study

object MyObjects {

    private val x = 2;

    val y = 3;

    fun ferry() {
        println("abcd")
    }
}

class Clazz {
    companion object {
        private const val MAN = 1

        private const val WOMAN = 2

        private fun isGender(x: Int) = x == MAN || x == WOMAN
    }

    fun toGender(x: Int): Int {
        if (isGender(x)) {
            return x
        }
        return -1
    }
}

fun main(args: Array<String>) {
    MyObjects.ferry()
    println(MyObjects.y)
    println(Clazz().toGender(2));
}