package servlet.app.controller

import servlet.app.dao.UserDao
import servlet.core.mvc.*
import servlet.core.mvc.LegacyController.Companion.jspView
import servlet.core.mvc.LegacyController.Companion.redirectView
import webserver.application.NotAuthenticationException
import webserver.application.NotAuthorizedException
import servlet.app.model.User
import servlet.app.model.UserNotFoundException
import servlet.core.annotation.Controller
import servlet.core.annotation.Inject
import webserver.application.repository.OUserRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class UserUpdateController @Inject constructor(private val userDao: UserDao) : LegacyController {

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
        val user = req.getParameter("userId")?.let { OUserRepository.get(it) } ?: throw UserNotFoundException()
        if (sessionUser.id != user.id) {
            throw NotAuthorizedException()
        }
        return sessionUser
    }
}