package lam.kotlin.study

fun main(args: Array<String>) {
    var a: Char = '1';
    if (a is Char) {
        println("a is Char");
    } else {
        println("a is not Char");
    }
    toCheckType(4);
}

fun toCheckType(any: Any?) {
    if (any is String) {
        println(any.length);
    } else {
        println("any is not String:" + any);
    }
}