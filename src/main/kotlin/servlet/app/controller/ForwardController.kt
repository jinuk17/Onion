package servlet.app.controller

import servlet.core.mvc.Controller
import servlet.core.mvc.Controller.Companion.jspView
import servlet.core.mvc.JspView
import servlet.core.mvc.ModelAndView
import servlet.core.mvc.View
import java.lang.RuntimeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ForwardController(private val viewName: String) : Controller {
    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView(viewName)
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        throw RuntimeException()
    }

}