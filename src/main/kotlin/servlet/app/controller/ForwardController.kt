package servlet.app.controller

import servlet.core.Controller
import java.lang.RuntimeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ForwardController(val viewName: String) : Controller {
    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        return viewName
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        throw RuntimeException()
    }

}