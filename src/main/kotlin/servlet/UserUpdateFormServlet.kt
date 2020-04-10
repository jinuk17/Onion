package servlet

import webserver.application.NotAuthenticationException
import webserver.application.NotAuthorizedException
import webserver.application.model.User
import webserver.application.model.UserNotFoundException
import webserver.application.repository.UserRepository
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet("/user/update")
class UserUpdateFormServlet: HttpServlet() {

    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {

        val user = getAuthorizedUser(req)
        req.setAttribute("user", user)
        req.getRequestDispatcher("/user/update.jsp").forward(req, resp)
    }

    override fun doPost(req: HttpServletRequest, resp: HttpServletResponse) {

        val user = getAuthorizedUser(req)
        val save = user.copy(
            password = req.getParameter("password") ?: user.password,
            name = req.getParameter("name") ?: user.name,
            email = req.getParameter("email") ?: user.email
        )
        UserRepository.save(save)

        resp.sendRedirect("/user/list")
    }

    private fun getAuthorizedUser(req: HttpServletRequest): User {
        val sessionUser = Authentication.getSessionUser(req) ?: throw NotAuthenticationException()
        val user = req.getParameter("userId")?.let { UserRepository.get(it) } ?: throw UserNotFoundException()
        if (sessionUser.id != user.id) { throw NotAuthorizedException()
        }
        return sessionUser
    }

}