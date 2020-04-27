package servlet.app.controller

import servlet.app.dao.UserDao
import servlet.core.Controller
import servlet.core.Controller.Companion.redirect
import servlet.core.NotFoundUrlException
import webserver.application.repository.UserRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserListController : Controller {

    private val userDao = UserDao()

    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {

        if (!Authentication.hasUserSession(request)) return redirect("/user/login")

        request.setAttribute("users", userDao.findAll())
        return "/user/list.jsp"
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        throw NotFoundUrlException()
    }
}