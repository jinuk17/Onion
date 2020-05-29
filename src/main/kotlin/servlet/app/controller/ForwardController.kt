package servlet.app.controller

import servlet.core.mvc.LegacyController
import servlet.core.mvc.LegacyController.Companion.jspView
import servlet.core.mvc.ModelAndView
import java.lang.RuntimeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ForwardController(private val viewName: String) : LegacyController {
    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView(viewName)
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        throw RuntimeException()
    }

}