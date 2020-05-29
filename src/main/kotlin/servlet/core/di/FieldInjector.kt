package servlet.core.di

import mu.KotlinLogging
import org.springframework.beans.BeanUtils
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.reflect.Constructor

class FieldInjector(private val beanFactory: BeanFactory) : Injector {

    private val logger = KotlinLogging.logger {}
    private val constructorInjector: ConstructorInjector = ConstructorInjector(beanFactory)

    override fun inject(clazz: Class<*>) {
        instantiateClass(clazz)
        BeanFactoryUtils.getInjectedFields(clazz).forEach {
            try {
                val bean = instantiateClass(it.type)
                it.isAccessible = true
                it.set(beanFactory.getBean(it.declaringClass), bean)
            } catch (e: Exception) {
                when (e) {
                    is IllegalAccessException, is IllegalArgumentException -> logger.error { e }
                    else -> throw e
                }
            }
        }
    }

    private fun instantiateClass(clazz: Class<*>): Any {
        constructorInjector.inject(clazz)
        return beanFactory.getBean(clazz)
    }
}