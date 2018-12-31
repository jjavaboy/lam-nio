package lam.kotlin.study

fun main(args: Array<String>) {
    var a = "";
    var b = "a";
    a?.equals(b) ?: (b == null);
}