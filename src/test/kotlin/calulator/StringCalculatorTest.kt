package calulator

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import java.lang.RuntimeException

class StringCalculatorTest {

    companion object {

        lateinit var cal: StringCalculator

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            println("BeforeAll")
            cal = StringCalculator()
        }
        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            println("afterAll")
        }
    }

    @Test
    fun add_null_or_empty_string() {
        assertEquals(0, cal.add(null))
        assertEquals(0, cal.add(""))
    }

    @Test
    fun add_a_number() {
        assertEquals(1, cal.add("1"))
    }

    @Test
    fun add_comma_separator() {
        assertEquals(3, cal.add("1,2"))
    }

    @Test
    fun add_comma_or_colon_separator() {
        assertEquals(6, cal.add("1,2:3"))
    }

    @Test
    fun add_custom_separator_with() {
        assertEquals(6, cal.add("//;\n1;2;3"))
    }

    @Test
    fun add_negative() {
        assertThrows(RuntimeException::class.java) {
            cal.add("-1;2;3")
        }
    }
}