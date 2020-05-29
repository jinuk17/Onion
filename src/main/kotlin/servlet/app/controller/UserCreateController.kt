package servlet.app.controller

import mu.KotlinLogging
import servlet.app.dao.UserDao
import servlet.core.mvc.*
import servlet.core.mvc.LegacyController.Companion.jspView
import servlet.core.mvc.LegacyController.Companion.redirectView
import servlet.app.model.User
import servlet.core.annotation.Controller
import servlet.core.annotation.Inject
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class UserCreateController @Inject constructor(private val userDao: UserDao) : LegacyController {

    private val logger = KotlinLogging.logger {}

    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView("/user/form.jsp")
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        val user = User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email")
        )

        try {
            userDao.insert(user)
        } catch (e: SQLException) {
            logger.error { e.message }
        }

        return redirectView("/user/list")
    }
}