package servlet.app.controller

import mu.KotlinLogging
import servlet.core.Controller
import servlet.core.Controller.Companion.redirect
import servlet.app.dao.UserDao
import webserver.application.model.User
import java.sql.SQLException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserCreateController : Controller {

    private val logger = KotlinLogging.logger {}

    private val userDao = UserDao()

    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        return "/user/form.jsp"
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {

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

        return redirect("/user/list")
    }
}