package servlet.core.di

import mu.KotlinLogging
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.reflect.InvocationTargetException

class SetterInjector(private val beanFactory: BeanFactory) : Injector {

    private val logger = KotlinLogging.logger {}
    private val constructorInjector: ConstructorInjector = ConstructorInjector(beanFactory)

    override fun inject(clazz: Class<*>) {
        instantiateClass(clazz)
        BeanFactoryUtils.getInjectedMethods(clazz).forEach {
            val parameterTypes = it.parameterTypes
            if (parameterTypes.size != 1) throw IllegalStateException("Setter DI must have one argument.")

            val bean = instantiateClass(clazz)

            try {
                it.invoke(beanFactory.getBean(it.declaringClass), bean)
            } catch (e: Exception) {
                when (e) {
                    is IllegalAccessException,
                    is IllegalArgumentException,
                    is InvocationTargetException -> logger.error { e }
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