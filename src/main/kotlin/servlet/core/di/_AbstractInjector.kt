package servlet.core.di

import org.springframework.beans.BeanUtils
import java.lang.IllegalStateException
import java.lang.reflect.Constructor

abstract class _AbstractInjector(private val beanFactory: BeanFactory) : Injector {

    override fun inject(clazz: Class<*>) {
        instantiateClass(clazz)
        getInjectedBeans(clazz).forEach {
            val beanClass = geBeanClass(it)
            inject(it, instantiateClass(beanClass), beanFactory)
        }
    }

    abstract fun getInjectedBeans(clazz: Class<*>): Set<Any>
    abstract fun geBeanClass(injectedBean: Any): Class<*>
    abstract fun inject(injectedBean: Any, bean: Any, beanFactory: BeanFactory)

    private fun instantiateClass(clazz: Class<*>): Any {
        return beanFactory.getOrRegister(clazz) {
            val injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz)
            injectedConstructor?.let { instantiateConstructor(it) } ?: clazz.newInstance()
        }
    }

    private fun instantiateConstructor(constructor: Constructor<*>): Any {
        val args = constructor.parameterTypes.map {
            if (!beanFactory.isPreInstantiateBean(it)) {
                throw IllegalStateException("$it is not a Bean.")
            }
            instantiateClass(it)
        }

        return BeanUtils.instantiateClass(constructor, *args.toTypedArray())
    }
}