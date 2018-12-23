package lam.kotlin.study

open class A {
    open fun a(): Unit { println("A.a()"); }

    fun b(): Unit { println("A.b()")}
}

interface I {
    fun a(): Unit;

    fun b(): Unit;
}

class B() : A(), I {
    override fun a(): Unit {
        super<A>.a()
        println("B.a()");
    }
}

class Bar constructor() : A() {
    fun ab() { println("Bar.ab()"); }

    inner class InterBar {
        fun abc() {
            //调用外部类Bar的超类A的a()
            super@Bar.a();
            println("Bar.InterBar.abc()");
        }
    }
}

fun main(args: Array<String>): Unit {
    var b: B = B();
    b.a();
    b.b();

    var bar: Bar = Bar();
    var interBar = bar.InterBar()
    bar.ab();
    interBar.abc();
}

