package lam.kotlin.study

class Fir {

    //属性success有默认的getter and setter
    var success: Boolean? = null;

    var code: Int = 0; get() { println("code.get()"); return field} set(value): Unit { println("code.set()"); field = value; }

}

fun main(args: Array<String>) {
    var fir: Fir = Fir();
    fir.success = true;
    fir.code = 2;
    println("success: ${fir.success}, code:${fir.code}");
}