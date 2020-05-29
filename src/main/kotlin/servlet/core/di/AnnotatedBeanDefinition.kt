package servlet.core.di

import java.lang.reflect.Method

class AnnotatedBeanDefinition(clazz: Class<*>, private val method: Method) : BeanDefinition(clazz) {

    fun getMethod(): Method {
        return method
    }
}
