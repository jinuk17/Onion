package servlet.core.di

import mu.KotlinLogging
import org.springframework.beans.BeanUtils
import servlet.core.annotation.Controller
import java.lang.IllegalStateException
import java.lang.reflect.Constructor


class BeanFactory(private val preInstantiateBeans: Set<Class<*>>) {

    private val logger = KotlinLogging.logger {}

    private val beans: MutableMap<Class<*>, Any> = mutableMapOf()

    init {
        val constructorInjector = ConstructorInjector(this)
        preInstantiateBeans.forEach { constructorInjector.inject(it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(requiredType: Class<T>): T {
        return beans[requiredType] as T
    }

    fun getControllers(): Map<Class<*>, Any> {
        return preInstantiateBeans
            .filter { it.getAnnotation(Controller::class.java) != null }
            .mapNotNull { beans[it]?.let { bean -> it to bean }  }
            .toMap()
    }

    fun getOrRegister(clazz: Class<*>, instantiateBean: () -> Any): Any {
        return beans.getOrPut(clazz) { instantiateBean() }
    }

    fun isPreInstantiateBean(clazz: Class<*>): Boolean {
        val concreteClass = BeanFactoryUtils.findConcreteClass(clazz, preInstantiateBeans)
        return preInstantiateBeans.contains(concreteClass)
    }
}