package servlet.reflection


class Junit3Test {

    @Throws(Exception::class)
    fun test1() {
        println("Running Test1")
    }

    @Throws(Exception::class)
    fun test2() {
        println("Running Test2")
    }

    @Throws(Exception::class)
    fun test3() {
        println("Running Test3")
    }

    @Throws(Exception::class)
    fun notTest() {
        println("Running notTest")
    }

}