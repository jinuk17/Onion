package servlet.app.controller

import servlet.app.dao.UserDao
import servlet.core.NotFoundUrlException
import servlet.core.annotation.Controller
import servlet.core.annotation.Inject
import servlet.core.mvc.*
import servlet.core.mvc.LegacyController.Companion.jspView
import servlet.core.mvc.LegacyController.Companion.redirectView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class UserListController @Inject constructor(private val userDao: UserDao) : LegacyController {

    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        if (!Authentication.hasUserSession(request)) return redirectView("/user/login")

        val users = userDao.findAll()
        return jspView("/user/list.jsp").addObject("users", users)
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        throw NotFoundUrlException()
    }
}