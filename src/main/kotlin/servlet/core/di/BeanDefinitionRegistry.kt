package servlet.core.di

interface BeanDefinitionRegistry {
    fun registerBeanDefinition(clazz: Class<*>, beanDefinition: BeanDefinition)
}