package servlet.reflection

import org.junit.jupiter.api.Test

class Junit3TestRunner {

    @Test
    fun run() {
        val clazz = Junit3Test::class.java
        val newInstance = clazz.newInstance()
        val declaredMethods = clazz.declaredMethods
        declaredMethods.filter { it.name.startsWith("test") }
            .forEach { it.invoke(newInstance) }
    }
}