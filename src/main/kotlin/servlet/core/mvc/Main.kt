package servlet.core.mvc

class Test(private val name: String) {

    fun get(): String {
        println(name)
        return name
    }
}

fun main(args: Array<String>){

    val find = listOf(
        Test("1"), Test("2"), Test("3")
    ).asSequence()
        .map {
        it.get()
    }.find { it == "2" }

    println(find)
}