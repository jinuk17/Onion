package servlet.core.mvc

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class ControllerScannerTest {

    private lateinit var cf: ControllerScanner

    @BeforeEach
    fun setup() {
        cf = ControllerScanner("servlet.core.mvc")
    }

    @Test
    fun getControllers() {
        val controllers: Map<Class<*>, Any> = cf.getControllers()
        Assertions.assertNotNull(controllers[MyController::class.java])
    }
}