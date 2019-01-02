package lam.kotlin.study

import org.w3c.dom.*
//import kotlin.browser.document
//import kotlin.browser.window

// https://www.kotlincn.net/docs/reference/type-safe-builders.html#comexamplehtml-%E5%8C%85%E7%9A%84%E5%AE%8C%E6%95%B4%E5%AE%9A%E4%B9%89
// https://play.kotlinlang.org/byExample/09_Kotlin_JS/06_HtmlBuilder

fun main(args: Array<String>) {
    /*
    val result = html {                                            // 1
        head {                                                     // 2
            title { +"HTML encoding with Kotlin" }
        }
        body {                                                     // 2
            h1 { +"HTML encoding with Kotlin" }
            p {
                +"this format can be used as an"                   // 3
                +"alternative markup to HTML"                      // 3
            }

            // an element with attributes and text content
            a(href = "http://jetbrains.com/kotlin") { +"Kotlin" }

            // mixed content
            p {
                +"This is some"
                b { +"mixed" }
                +"text. For more see the"
                a(href = "http://jetbrains.com/kotlin") {
                    +"Kotlin"
                }
                +"project"
            }
            p {
                +"some text"
                ul {
                    for (i in 1..5)
                        li { +"${i}*2 = ${i*2}" }
                }
            }
        }
    }


    document.body!!.appendChild(result.element)
    */
}

/*
abstract class Tag(val name: String) {
    val element = document.createElement(name)
    protected fun <T : Tag> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        element.appendChild(tag.element)
        return tag
    }
}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        element.appendChild(document.createTextNode(this + " "))
    }
}

class HTML() : TagWithText("html") {
    fun head(init: Head.() -> Unit) = initTag(Head(), init)
    fun body(init: Body.() -> Unit) = initTag(Body(), init)
}


class Head() : TagWithText("head") {
    fun title(init: Title.() -> Unit) = initTag(Title(), init)
}

class Title() : TagWithText("title")

abstract class BodyTag(name: String) : TagWithText(name) {
    fun b(init: B.() -> Unit) = initTag(B(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun h1(init: H1.() -> Unit) = initTag(H1(), init)
    fun ul(init: UL.() -> Unit) = initTag(UL(), init)
    fun a(href: String, init: A.() -> Unit) {
        val a = initTag(A(), init)
        a.href = href
    }
}

class Body() : BodyTag("body")
class UL() : BodyTag("ul") {
    fun li(init: LI.() -> Unit) = initTag(LI(), init)
}

class B() : BodyTag("b")
class LI() : BodyTag("li")
class P() : BodyTag("p")
class H1() : BodyTag("h1")

class A() : BodyTag("a") {
    public var href: String
        get() = element.getAttribute("href")!!
        set(value) {
            element.setAttribute("href", value)
        }
}

fun html(init: HTML.() -> Unit): HTML {
    val html = HTML()
    html.init()
    return html
}*/
