package servlet.controller

import servlet.Controller
import servlet.Controller.Companion.redirect
import servlet.NotFoundUrlException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogoutController: Controller {
    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        request.session.removeAttribute("user")
        return redirect("/index.jsp")
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        throw NotFoundUrlException()
    }
}