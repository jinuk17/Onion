package servlet.core.di

import org.springframework.beans.BeanUtils
import java.lang.IllegalStateException
import java.lang.reflect.Constructor

class ConstructorInjector(private val beanFactory: BeanFactory) : Injector {

    override fun inject(clazz: Class<*>) {
        instantiateClass(clazz)
    }

    private fun instantiateClass(clazz: Class<*>): Any {
        return beanFactory.getOrRegister(clazz) {
            val injectedConstructor = BeanFactoryUtils.getInjectedConstructor(clazz)
            injectedConstructor?.let { instantiateConstructor(it) } ?: clazz.newInstance()
        }
    }

    private fun instantiateConstructor(constructor: Constructor<*>): Any {
        val args = constructor.parameterTypes.map {
            if(!beanFactory.isPreInstantiateBean(it)){
                throw IllegalStateException("$it is not a Bean.")
            }
            instantiateClass(it)
        }

        return BeanUtils.instantiateClass(constructor, *args.toTypedArray())
    }
}