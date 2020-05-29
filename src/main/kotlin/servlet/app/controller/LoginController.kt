package servlet.app.controller

import servlet.core.mvc.*
import servlet.core.mvc.Controller.Companion.jspView
import servlet.core.mvc.Controller.Companion.redirectView
import servlet.app.model.Login
import webserver.application.service.UserService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoginController : Controller {

    private val userService = UserService()

    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView("/user/login.jsp")
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        val login = Login(
            request.getParameter("userId"),
            request.getParameter("password")
        )

        val user = userService.login(login) ?: return jspView("/user/login.jsp").addObject("loginFailed", true)

        request.session.setAttribute("user", user)
        return redirectView("/index.jsp")
    }
}