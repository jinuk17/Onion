package servlet.core.mvc

import mu.KotlinLogging
import org.reflections.Reflections
import servlet.core.annotation.Controller

class ControllerScanner(vararg path: Any) {

    private val logger = KotlinLogging.logger {}
    private val reflections: Reflections = Reflections(path)

    fun getControllers(): Map<Class<*>, Any> {
        val annotated = reflections.getTypesAnnotatedWith(Controller::class.java)
        return annotated.mapNotNull { instantiateControllers(it) }.toMap()
    }

    private fun instantiateControllers(clazz: Class<*>): Pair<Class<*>, Any>? {
        return try {
            clazz to clazz.newInstance()
        } catch (e: Exception) {
            when (e) {
                is InstantiationException, is IllegalAccessException -> {
                    logger.error { e }
                    null
                }
                else -> throw e
            }
        }
    }
}
