package servlet.core.mvc

import mu.KotlinLogging
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HandlerExecution(
    private val declareObject: Any,
    private val method: Method
) {
    private val logger = KotlinLogging.logger {}

    fun handle(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        try {
            return method.invoke(declareObject, request, response) as ModelAndView
        } catch (e: Exception) {
            when (e) {
                is IllegalArgumentException, is IllegalAccessException, is InvocationTargetException -> {
                    logger.error("{} method invoke fail. error message : {}", method, e.message)
                    throw RuntimeException(e)
                }
                else -> throw e
            }
        }
    }
}
