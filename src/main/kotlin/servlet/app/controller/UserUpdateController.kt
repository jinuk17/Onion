package servlet.app.controller

import servlet.app.dao.UserDao
import servlet.core.mvc.*
import servlet.core.mvc.Controller.Companion.jspView
import servlet.core.mvc.Controller.Companion.redirectView
import webserver.application.NotAuthenticationException
import webserver.application.NotAuthorizedException
import webserver.application.model.User
import webserver.application.model.UserNotFoundException
import webserver.application.repository.UserRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserUpdateController : Controller {

    private val userDao = UserDao()

    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        val user = getAuthorizedUser(request)
        request.setAttribute("user", user)
        return jspView("/user/update.jsp")
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        val user = getAuthorizedUser(request)
        val save = user.copy(
            password = request.getParameter("password") ?: user.password,
            name = request.getParameter("name") ?: user.name,
            email = request.getParameter("email") ?: user.email
        )
        userDao.update(save)
        return redirectView("/user/list")
    }

    private fun getAuthorizedUser(req: HttpServletRequest): User {
        val sessionUser = Authentication.getSessionUser(req) ?: throw NotAuthenticationException()
        val user = req.getParameter("userId")?.let { UserRepository.get(it) } ?: throw UserNotFoundException()
        if (sessionUser.id != user.id) {
            throw NotAuthorizedException()
        }
        return sessionUser
    }
}