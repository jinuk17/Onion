package servlet.app.controller

import servlet.core.mvc.LegacyController
import servlet.core.NotFoundUrlException
import servlet.core.mvc.LegacyController.Companion.redirectView
import servlet.core.mvc.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogoutController : LegacyController {
    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        request.session.removeAttribute("user")
        return redirectView("/index.jsp")
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        throw NotFoundUrlException()
    }
}