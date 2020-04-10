package servlet

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/user/logout")
class LogoutServlet: HttpServlet() {
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse){
        req.session.removeAttribute("user")
        resp.sendRedirect("/index.jsp")
    }
}