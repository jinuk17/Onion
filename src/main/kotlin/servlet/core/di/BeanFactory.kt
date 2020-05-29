package servlet.core.di

import mu.KotlinLogging
import org.springframework.beans.BeanUtils
import servlet.core.di.InjectType.*
import java.lang.IllegalStateException
import java.lang.reflect.Field

class BeanFactory : BeanDefinitionRegistry {

    private val logger = KotlinLogging.logger {}

    private val beans: MutableMap<Class<*>, Any> = mutableMapOf()
    private val beanDefinitions: MutableMap<Class<*>, BeanDefinition> = mutableMapOf()

    fun initialize() {
        getBeanClasses().forEach { getBean(it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(requiredType: Class<T>): T {
        return beans.getOrPut(requiredType) {
            val findConcreteClass = findConcreteClass(requiredType)
            val bean = beanDefinitions[findConcreteClass] ?: throw IllegalStateException("$requiredType is not a Bean.")
            inject(bean)
        } as T
    }

    fun getOrRegister(clazz: Class<*>, instantiateBean: () -> Any): Any {
        return beans.getOrPut(clazz) { instantiateBean() }
    }

    fun isPreInstantiateBean(clazz: Class<*>): Boolean {
        val beanClasses = getBeanClasses()
        val concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses)
        return beanClasses.contains(concreteClass)
    }

    fun getBeanClasses(): Set<Class<*>> {
        return beanDefinitions.keys.toSet()
    }

    override fun registerBeanDefinition(clazz: Class<*>, beanDefinition: BeanDefinition) {
        logger.debug { "register bean : $clazz" }
        beanDefinitions[clazz] = beanDefinition
    }

    private fun findConcreteClass(clazz: Class<*>): Class<*> {
        val beanClasses: Set<Class<*>> = getBeanClasses()
        val concreteClazz = BeanFactoryUtils.findConcreteClass(clazz, beanClasses)
        if (concreteClazz?.let { beanClasses.contains(it) } != true) {
            throw IllegalStateException("$clazz is not a Bean.")
        }
        return concreteClazz
    }

    private fun inject(beanDefinition: BeanDefinition): Any {
        return when (beanDefinition.getResolvedInjectMode()) {
            INJECT_NO -> beanDefinition.getBeanClass().newInstance()
            INJECT_FIELD -> injectFields(beanDefinition)
            else -> injectConstructor(beanDefinition)
        }
    }

    private fun injectConstructor(beanDefinition: BeanDefinition): Any {
        val injectConstructor = beanDefinition.getInjectConstructor()!!
        val args = injectConstructor.parameterTypes.orEmpty().map { getBean(it) }
        return BeanUtils.instantiateClass(injectConstructor, *args.toTypedArray())
    }

    private fun injectFields(beanDefinition: BeanDefinition): Any {
        val bean = beanDefinition.getBeanClass().newInstance()
        beanDefinition.getInjectFields().forEach { injectField(bean, it) }
        return bean
    }

    private fun injectField(bean: Any, field: Field) {
        logger.debug("Inject Bean : {}, Field : {}", bean, field)
        try {
            field.apply {
                isAccessible = true
                set(getBean(declaringClass), bean)
            }
        } catch (e: Exception) {
            when (e) {
                is IllegalAccessException, is IllegalArgumentException -> logger.error { e }
                else -> throw e
            }
        }
    }

}