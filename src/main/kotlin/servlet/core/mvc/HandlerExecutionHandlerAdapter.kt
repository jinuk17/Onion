package servlet.core.mvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HandlerExecutionHandlerAdapter : HandlerAdapter {
    override fun supports(handler: Any): Boolean = handler is HandlerExecution

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): ModelAndView {
        return (handler as HandlerExecution).handle(request, response)
    }
}