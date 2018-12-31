package lam.kotlin.study

fun main(args: Array<String>) {
    var a: Char = '1';
    if (a is Char) {
        println("a is Char");
    } else {
        println("a is not Char");
    }
    toCheckType("123");
    toCheckType(null);
}

fun toCheckType(any: Any?) {
    if (any is String) {
        println("String length:" + any.length);
    } else {
        println("any is not String:" + any);
    }
    if (any !is Int) {
        println("any is not Int:" + any);
    }
}

// List<*> == List<Any?> * 和 Any? 等价
fun handleStringList(list: List<*>) {
    if (list is ArrayList) {
        list.forEach {

        }
    }
}