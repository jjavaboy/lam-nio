package lam.kotlin.study;

class MyClass(var id: Long, var name: String, var gender: Int) {

}

fun main(args: Array<String>) {
    var myClass: MyClass = MyClass(0, "LAM", 1);
    println("id:" + myClass.id + ", name:" + myClass.name + ", gender:" + myClass.gender);
    myFun(null);
    println("yourFun: ${yourFun(3)}");
    var list = arrayOf(3, 4, 5);
    println("asList: ${asList(1, 2, *list, 3)}");
}

fun myFun(i: Int? = 2): Unit {
    println("i:" + i);
    return ;
}

fun yourFun(i: Long): Long = i + 2L;

fun <T> asList(vararg ts: T): List<T> {
    var list = ArrayList<T>();
    for (t in ts) {
        list.add(t);
    }
    return list;
}