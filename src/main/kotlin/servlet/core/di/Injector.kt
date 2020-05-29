package servlet.core.di

interface Injector {
    fun inject(clazz: Class<*>)
}