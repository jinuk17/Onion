package servlet.core.di

import org.reflections.ReflectionUtils
import servlet.core.annotation.Inject
import java.lang.reflect.Constructor
import java.lang.reflect.Field


object BeanFactoryUtils {

    fun getInjectedConstructor(clazz: Class<*>?): Constructor<*>? {
        val injectedConstructors =
            ReflectionUtils.getAllConstructors(clazz, ReflectionUtils.withAnnotation(Inject::class.java))
        return if (injectedConstructors.isEmpty()) {
            null
        } else injectedConstructors.iterator().next()
    }

    fun findConcreteClass(injectedClazz: Class<*>, preInstanticateBeans: Set<Class<*>>): Class<*>? {
        if (!injectedClazz.isInterface) {
            return injectedClazz
        }
        for (clazz in preInstanticateBeans) {
            val interfaces: Set<Class<*>> = hashSetOf(*clazz.interfaces)
            if (interfaces.contains(injectedClazz)) {
                return clazz
            }
        }
        throw IllegalStateException(injectedClazz.toString() + "인터페이스를 구현하는 Bean이 존재하지 않는다.")
    }

    fun getInjectedFields(clazz: Class<*>): Set<Field>  {
        return ReflectionUtils.getAllFields(clazz, ReflectionUtils.withAnnotation(Inject::class.java))
    }
}