package servlet.core.di

import mu.KotlinLogging
import java.lang.reflect.Field

class FieldInjector(beanFactory: BeanFactory) : AbstractInjector<Field>(beanFactory) {

    private val logger = KotlinLogging.logger {}

    override fun inject(injectedType: Field, bean: Any, beanFactory: BeanFactory) {
        try {
            injectedType.apply {
                isAccessible = true
                set(beanFactory.getBean(declaringClass), bean)
            }
        } catch (e: Exception) {
            when (e) {
                is IllegalAccessException, is IllegalArgumentException -> logger.error { e }
                else -> throw e
            }
        }
    }

    override fun getInjectedBeans(clazz: Class<*>): Set<Field> = BeanFactoryUtils.getInjectedFields(clazz)

    override fun instantiateBean(injectedType: Field): Any? = instantiateClass(injectedType.type)
}