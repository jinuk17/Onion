package servlet.core.di

import org.reflections.Reflections
import servlet.core.annotation.Controller
import servlet.core.annotation.Repository
import servlet.core.annotation.Service

class ClasspathBeanDefinitionScanner(private val beanDefinitionRegistry: BeanDefinitionRegistry) {

    fun doScan(vararg basePackage: Any) {
        val reflections = Reflections(basePackage)
        getTypesAnnotatedWith(reflections, Controller::class.java, Service::class.java, Repository::class.java).forEach {
            beanDefinitionRegistry.registerBeanDefinition(it, BeanDefinition(it))
        }
    }

    private fun getTypesAnnotatedWith(reflections: Reflections, vararg annotations: Class<out Annotation>): Set<Class<*>> {
        return annotations.flatMap {
            reflections.getTypesAnnotatedWith(it)
        }.toSet()
    }

}