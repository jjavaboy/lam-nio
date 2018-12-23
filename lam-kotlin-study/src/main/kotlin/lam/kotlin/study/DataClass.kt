package lam.kotlin.study

// data用来修饰只保存数据的类
data class MyData (var id: Long = 0, var name: String = "") {

}

fun main(args: Array<String>) {
    var myData = MyData(1, "LAM");
    println(myData);

    var myData0 = MyData();
    myData0.name = "new name";
    println(myData0);

    // kotlin自带copy复制方法。
    var myData1 = myData0.copy(1);
    println(myData1);

    // kotlin自带的成对类
    var p = Pair<Int, String>(1, "NAME");
    println(p);

    // kotlin自带的三个属性字段类
    var t = Triple<Int, Boolean, Long>(2, true, 3L);
    println(t);
}