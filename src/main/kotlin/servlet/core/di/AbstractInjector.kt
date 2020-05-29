package servlet.core.di

import org.springframework.beans.BeanUtils
import java.lang.IllegalStateException
import java.lang.reflect.Constructor


abstract class AbstractInjector<T>(private val beanFactory: BeanFactory) : Injector {

    override fun inject(clazz: Class<*>) {
        instantiateClass(clazz)
        getInjectedBeans(clazz).forEach { injectBean ->
            instantiateBean(injectBean)?.let { inject(injectBean, it, beanFactory) }
        }
    }

    abstract fun getInjectedBeans(clazz: Class<*>): Set<T>
    abstract fun instantiateBean(injectedType: T): Any?
    abstract fun inject(injectedType: T, bean: Any, beanFactory: BeanFactory)

    protected fun instantiateClass(clazz: Class<*>): Any {
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