package servlet.reflection


class Junit4Test {


    @MyTest
    @Throws(Exception::class)
    fun one() {
        println("Running one")
    }

    @MyTest
    @Throws(Exception::class)
    fun two() {
        println("Running two")
    }

    @Throws(Exception::class)
    fun three() {
        println("Running three")
    }
}