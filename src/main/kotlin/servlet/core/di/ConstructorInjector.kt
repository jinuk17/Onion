package servlet.core.di

class ConstructorInjector(beanFactory: BeanFactory) : AbstractInjector<Any>(beanFactory) {

    override fun inject(injectedType: Any, bean: Any, beanFactory: BeanFactory) {}

    override fun getInjectedBeans(clazz: Class<*>): Set<Any> = setOf()

    override fun instantiateBean(injectedType: Any): Any? = null
}