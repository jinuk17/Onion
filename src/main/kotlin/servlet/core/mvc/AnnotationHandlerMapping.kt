package servlet.core.mvc

import mu.KotlinLogging
import org.reflections.ReflectionUtils
import servlet.core.annotation.RequestMapping
import servlet.core.annotation.RequestMethod
import servlet.core.di.BeanFactory
import javax.servlet.http.HttpServletRequest

class AnnotationHandlerMapping(vararg basePackage: Any) : HandlerMapping {

    val logger = KotlinLogging.logger {}

    private val handlerExecutions: Map<HandleKey, HandlerExecution>

    init {
        val beanScanner = BeanScanner(basePackage)
        val beanFactory = BeanFactory(beanScanner.scan())
        handlerExecutions = beanFactory.getControllers().keys.flatMap { clazz ->
            ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withAnnotation(RequestMapping::class.java))
                .mapNotNull { method ->
                    val rm = method.getAnnotation(RequestMapping::class.java)
                    HandleKey(rm.value, rm.method) to HandlerExecution(method.declaringClass, method)
                }
        }.toMap()
    }

    override fun getHandler(request: HttpServletRequest): HandlerExecution? {
        val handleKey = request.toHandleKey()
        logger.debug("handleKey : $handleKey")
        return handlerExecutions[handleKey]
    }

    /*
    * https://kotlinlang.org/docs/reference/extensions.html#extensions
    * */
    private fun HttpServletRequest.toHandleKey(): HandleKey {
        val requestURI = this.requestURI
        val requestMethod = RequestMethod.valueOf(this.method.toUpperCase())
        return HandleKey(requestURI, requestMethod)
    }
}
