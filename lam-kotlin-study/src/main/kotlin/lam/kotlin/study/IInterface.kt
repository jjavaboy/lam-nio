package lam.kotlin.study

interface MyInterface {
    // 接口的属性是抽象的
    val a: Int;

    val b: Int get() = 0;

    fun bar();

    fun foo() {
        println("MyInterface.foo()");
    }
}

class MyInterfaceImpl : MyInterface {
    override val a: Int = 0;
    override fun bar() {
        println("MyInterfaceImpl.bar()");
    }
}