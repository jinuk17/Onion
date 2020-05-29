package servlet.core.di

import mu.KotlinLogging
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class SetterInjector(beanFactory: BeanFactory) : AbstractInjector<Method>(beanFactory) {

    private val logger = KotlinLogging.logger {}

    override fun inject(injectedType: Method, bean: Any, beanFactory: BeanFactory) {
        try {
            injectedType.apply { invoke(beanFactory.getBean(declaringClass), bean) }
        } catch (e: Exception) {
            when (e) {
                is IllegalAccessException,
                is IllegalArgumentException,
                is InvocationTargetException -> logger.error { e }
                else -> throw e
            }
        }
    }

    override fun getInjectedBeans(clazz: Class<*>): Set<Method> {
        return BeanFactoryUtils.getInjectedMethods(clazz)
    }

    override fun instantiateBean(injectedType: Method): Any? {
        val parameterTypes = injectedType.parameterTypes
        if (parameterTypes.size != 1) throw IllegalStateException("Setter DI must have one argument.")
        return instantiateClass(parameterTypes[0])
    }
}