package servlet.app.controller

import servlet.core.Controller
import servlet.core.Controller.Companion.redirect
import webserver.application.model.Login
import webserver.application.service.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginController : Controller {

    private val userService = UserService()

    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        return "/user/login.jsp"
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        val login = Login(
            request.getParameter("userId"),
            request.getParameter("password")
        )

        val user = userService.login(login) ?: return loginFailed(request)

        request.session.setAttribute("user", user)
        return redirect("/index.jsp")
    }

    private fun loginFailed(req: HttpServletRequest): String {
        req.setAttribute("loginFailed", true)
        return "/user/login.jsp"
    }
}