package servlet.core.di

class ApplicationContext(vararg basePackage: Any) {

    private val beanFactory: BeanFactory = BeanFactory()
    init {
        val scanner = ClasspathBeanDefinitionScanner(beanFactory)
        scanner.doScan(basePackage)
        beanFactory.initialize()
    }

    fun <T> getBean(clazz: Class<T>): T? = beanFactory.getBean(clazz)
    fun getBeanClasses(): Set<Class<*>> = beanFactory.getBeanClasses()
}