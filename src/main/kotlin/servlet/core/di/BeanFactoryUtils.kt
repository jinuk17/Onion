package servlet.core.di

import org.reflections.ReflectionUtils
import servlet.core.annotation.Inject
import java.lang.reflect.Constructor
import java.lang.reflect.Field
import java.lang.reflect.Method


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
        throw IllegalStateException("Not fount concreteClass of ${injectedClazz.toString()}")
    }

    fun getInjectedFields(clazz: Class<*>): Set<Field>  {
        return ReflectionUtils.getAllFields(clazz, ReflectionUtils.withAnnotation(Inject::class.java))
    }

    fun getInjectedMethods(clazz: Class<*>): Set<Method> {
        return ReflectionUtils.getAllMethods(
            clazz,
            ReflectionUtils.withAnnotation(Inject::class.java),
            ReflectionUtils.withReturnType(Unit::class.java)
        ).orEmpty().toSet()
    }
}