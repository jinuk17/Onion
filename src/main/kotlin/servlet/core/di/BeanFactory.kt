package servlet.core.di

import mu.KotlinLogging
import org.springframework.beans.BeanUtils
import servlet.core.annotation.Controller
import java.lang.IllegalStateException
import java.lang.reflect.Constructor

class BeanFactory : BeanDefinitionRegistry {

    private val logger = KotlinLogging.logger {}

    private val beans: MutableMap<Class<*>, Any> = mutableMapOf()
    private val beanDefinitions: MutableMap<Class<*>, BeanDefinition> = mutableMapOf()
    private val injectors: List<Injector>

    init {
        injectors = listOf(
            FieldInjector(this),
            SetterInjector(this),
            ConstructorInjector(this)
        )
    }

    fun initialize() {
        getBeanClasses().forEach { getBean(it) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getBean(requiredType: Class<T>): T {
        return beans[requiredType] as T
    }

    fun getControllers(): Map<Class<*>, Any> {
        return getBeanClasses()
            .filter { it.getAnnotation(Controller::class.java) != null }
            .mapNotNull { beans[it]?.let { bean -> it to bean }  }
            .toMap()
    }

    fun getOrRegister(clazz: Class<*>, instantiateBean: () -> Any): Any {
        return beans.getOrPut(clazz) { instantiateBean() }
    }

    fun isPreInstantiateBean(clazz: Class<*>): Boolean {
        val beanClasses = getBeanClasses()
        val concreteClass = BeanFactoryUtils.findConcreteClass(clazz, beanClasses)
        return beanClasses.contains(concreteClass)
    }

    private fun inject(clazz: Class<*>) {
        injectors.forEach { it.inject(clazz) }
    }

    private fun getBeanClasses(): Set<Class<*>> {
        return beanDefinitions.keys.toSet()
    }

    override fun registerBeanDefinition(clazz: Class<*>, beanDefinition: BeanDefinition) {
        logger.debug { "register bean : $clazz" }
        beanDefinitions[clazz] = beanDefinition
    }
}