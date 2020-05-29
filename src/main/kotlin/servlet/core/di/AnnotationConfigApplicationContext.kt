package servlet.core.di

import mu.KotlinLogging
import servlet.core.annotation.ComponentScan

class AnnotationConfigApplicationContext(vararg annotatedClasses: Class<*>) : ApplicationContext {

    private val logger = KotlinLogging.logger {}
    private val beanFactory = BeanFactory()

    init {
        val _annotatedClasses = annotatedClasses.toList().toTypedArray()
        val basePackages = findBasePackages(_annotatedClasses)
        val annotatedBeanDefinitionReader = AnnotatedBeanDefinitionReader(beanFactory)
        annotatedBeanDefinitionReader.register(_annotatedClasses)
        if (basePackages.isNotEmpty()) {
            val scanner = ClasspathBeanDefinitionScanner(beanFactory)
            scanner.doScan(*basePackages)
        }
        beanFactory.initialize()
    }

    private fun findBasePackages(annotatedClasses: Array<Class<*>>): Array<Any> {
        return annotatedClasses
            .mapNotNull { it.getAnnotation(ComponentScan::class.java) }
            .flatMap { it.value.toList() }
            .map {
                logger.info("Component Scan basePackage : {}", it)
                it
            }
            .toTypedArray()
    }

    override fun <T> getBean(clazz: Class<T>): T = beanFactory.getBean(clazz)
    override fun getBeanClasses(): Set<Class<*>> = beanFactory.getBeanClasses()

}