package servlet.core.di

import mu.KotlinLogging
import servlet.core.annotation.Bean

class AnnotatedBeanDefinitionReader(private val beanDefinitionRegistry: BeanDefinitionRegistry) {

    private val logger = KotlinLogging.logger {}

    fun register(annotatedClasses: Array<Class<*>>) {
        annotatedClasses.forEach { registerBean(it) }
    }

    private fun registerBean(annotatedClass: Class<*>) {
        beanDefinitionRegistry.registerBeanDefinition(annotatedClass, BeanDefinition(annotatedClass))
        BeanFactoryUtils.getBeanMethods(annotatedClass, Bean::class.java).forEach {
            logger.debug("@Bean method : {}", it)
            val annotatedBeanDefinition = AnnotatedBeanDefinition(it.returnType, it)
            beanDefinitionRegistry.registerBeanDefinition(it.returnType, annotatedBeanDefinition)
        }

    }
}
