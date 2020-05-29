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
        preInstantiateBeans.forEach { instantiateClass(it) }
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

    private fun instantiateClass(clazz: Class<*>): Any {
        return beans.getOrPut(clazz) {
            val injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz)
                ?: return clazz.newInstance()

            return instantiateConstructor(injectedConstructor)
        }
    }

    private fun instantiateConstructor(constructor: Constructor<*>): Any {

        val args = constructor.parameterTypes.map {
            val concreteClass = BeanFactoryUtils.findConcreteClass(it, preInstantiateBeans)
            if (!preInstantiateBeans.contains(concreteClass)) {
                throw IllegalStateException("$it is not a Bean.")
            }
            instantiateClass(it)
        }

        return BeanUtils.instantiateClass(constructor, *args.toTypedArray())
    }
}