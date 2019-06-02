package lam.kotlin.study

interface Foo {
    open var m: String
    fun print()
}

class FooImpl(m: String) : Foo {

    override var m = m

    override fun print() {
        println(m)
    }
}

class Delegation(foo: Foo): Foo by foo {
    override var m = "m in FooImpl"
}

fun main(args: Array<String>) {
    val foo = FooImpl("lalala")
    val dele = Delegation(foo)
    dele.print()
}