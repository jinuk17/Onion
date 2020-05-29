package servlet.core.mvc

import org.reflections.Reflections
import servlet.core.annotation.Controller
import servlet.core.annotation.Repository
import servlet.core.annotation.Service

class BeanScanner(vararg basePackage: Any) {

    private val reflections : Reflections = Reflections(basePackage)

    fun scan(): Set<Class<*>> = getTypesAnnotatedWith(Controller::class.java, Service::class.java, Repository::class.java)

    private fun getTypesAnnotatedWith(vararg annotations: Class<out Annotation>): Set<Class<*>> {
        return annotations.flatMap {
            reflections.getTypesAnnotatedWith(it)
        }.toSet()
    }

}