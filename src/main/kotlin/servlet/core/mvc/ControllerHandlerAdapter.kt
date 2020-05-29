package servlet.core.mvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ControllerHandlerAdapter : HandlerAdapter {

    override fun supports(handler: Any): Boolean = handler is LegacyController

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): ModelAndView {
        val controller = handler as LegacyController
        return if (request.method == "POST") controller.post(request, response) else controller.get(request, response)
    }
}