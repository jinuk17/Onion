package servlet.reflection

import org.junit.jupiter.api.Test

class Junit4TestRunner {

    @Test
    fun run() {
        val clazz = Junit4Test::class.java
        val newInstance = clazz.newInstance()
        clazz.methods
            .filter { it.isAnnotationPresent(MyTest::class.java) }
            .forEach { it.invoke(newInstance) }
    }
}