package servlet.core.di

import java.lang.IllegalStateException
import java.lang.reflect.Constructor
import java.lang.reflect.Field

class BeanDefinition(private val clazz: Class<*>) {

    private val injectConstructor: Constructor<*>?
    private val injectFields: Set<Field>

    init {
        injectConstructor = getInjectConstructor(clazz)
        injectFields = getInjectFields(clazz, injectConstructor)
    }

    fun getInjectConstructor(): Constructor<*>? {
        return injectConstructor
    }

    fun getInjectFields(): Set<Field> {
        return this.injectFields
    }

    fun getBeanClass(): Class<*> {
        return this.clazz
    }

    fun getResolvedInjectMode(): InjectType {
        return when {
            injectConstructor != null -> InjectType.INJECT_CONSTRUCTOR
            injectFields.isNotEmpty() -> InjectType.INJECT_FIELD
            else -> InjectType.INJECT_NO
        }
    }

    private fun getInjectConstructor(clazz: Class<*>): Constructor<*>? {
        return BeanFactoryUtils.getInjectedConstructor(clazz)
    }

    private fun getInjectFields(clazz: Class<*>, constructor: Constructor<*>?): Set<Field> {
        if (constructor != null) {
            return setOf()
        }
        val injectProperties = getInjectPropertiesType(clazz)
        return clazz.declaredFields.filter { injectProperties.contains(it.type) }.toSet()
    }

    private fun getInjectPropertiesType(clazz: Class<*>): Set<Class<*>> {
        return BeanFactoryUtils.getInjectedMethods(clazz).map {
            val parameterTypes = it.parameterTypes
            if (parameterTypes.size != 1) throw IllegalStateException("Setter DI must have one argument.")
            parameterTypes[0]
        }.toSet() + BeanFactoryUtils.getInjectedFields(clazz).map { it.type }.toSet()
    }
}
