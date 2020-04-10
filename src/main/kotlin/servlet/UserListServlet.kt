package servlet

import webserver.application.repository.UserRepository
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/user/list")
class UserListServlet: HttpServlet() {

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {

        if(!Authentication.hasUserSession(req)) {
            resp.sendRedirect("/users/login")
            return
        }

        req.setAttribute("users", UserRepository.getAll())
        val requestDispatcher = req.getRequestDispatcher("/user/list.jsp")
        requestDispatcher.forward(req, resp)
    }
}