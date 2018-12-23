package lam.kotlin.study

fun MutableList<Int>.swap(index1: Int, index2: Int) {
    // this关键词表示 该 List
    val tmp = this[index1];
    this[index1] = this[index2];
    this[index2] = tmp;
}

fun Any?.toString(): String {
    if (this == null) {
        return "null";
    }
    return this.toString();
}

open class C

class D : C() {
    open var a: Int = 2;
}

// 扩展函数
fun C.foo() = "C.foo"

fun D.foo() = "D.foo"

fun p(c: C): Unit {
    println(c.foo());
}

fun main(args: Array<String>) {
    var list = mutableListOf<Int>(1, 2, 3);
    list.swap(0, 1);
    println(list);

    p(D());

    var list1: List<String>? = null;
    println("list1:${list1}");
}