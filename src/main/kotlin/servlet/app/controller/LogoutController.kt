package servlet.app.controller

import servlet.core.Controller
import servlet.core.Controller.Companion.redirect
import servlet.core.NotFoundUrlException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogoutController : Controller {
    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        request.session.removeAttribute("user")
        return redirect("/index.jsp")
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        throw NotFoundUrlException()
    }
}