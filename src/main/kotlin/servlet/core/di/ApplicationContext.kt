package servlet.core.di

interface ApplicationContext {
    fun <T> getBean(clazz: Class<T>): T
    fun getBeanClasses(): Set<Class<*>>
}