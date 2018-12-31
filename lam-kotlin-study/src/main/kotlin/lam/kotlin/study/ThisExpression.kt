package lam.kotlin.study

class ThisClass (
        var i: Int
){
    fun addI(j: Int): Int {
        // this指的是类当前对象
        return this.i + j;
    }
}

// https://www.kotlincn.net/docs/reference/this-expressions.html

// 隐含标签@My
class My {

    // 隐含标签@MyInner
    inner class MyInner {

        // 隐含标签@foo
        fun Int.foo() {
            // My的标签
            var a = this@My;
            // MyInner的标签
            var b = this@MyInner;

            // this表示foo()的接收者：Int
            val c  = this;
            // 或者指定标签@foo的接收者：Int
            val c1 = this@foo

            val funList = lambda@ fun String.() {
                // funList的接收者
                val d = this;
            }

            val funList2 = {
                s: String ->
                // foo() 的接收者，因为它包含的 lambda 表达式
                // 没有任何接收者
                var d1 = this
            }
        }

    }

}