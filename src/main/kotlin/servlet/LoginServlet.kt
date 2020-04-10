package servlet

import webserver.application.model.Login
import webserver.application.service.UserService
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@WebServlet("/user/login")
class LoginServlet:HttpServlet() {

    private val userService = UserService()

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        req.getRequestDispatcher("/user/login.jsp").forward(req, resp)
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {
        val login = Login(
            req.getParameter("userId"),
            req.getParameter("password")
        )

        val user = userService.login(login) ?: return loginFailed(req, resp)

        req.session.setAttribute("user", user)
        resp.sendRedirect("/index.jsp")
    }

    private fun loginFailed(req: HttpServletRequest, resp: HttpServletResponse) {
        req.setAttribute("loginFailed", true)
        req.getRequestDispatcher("/user/login.jsp").forward(req, resp)
    }

}