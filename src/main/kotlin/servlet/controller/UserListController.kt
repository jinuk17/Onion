package servlet.controller

import servlet.Controller
import servlet.Controller.Companion.redirect
import servlet.NotFoundUrlException
import webserver.application.repository.UserRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserListController: Controller {

    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {

        if(!Authentication.hasUserSession(request)) return redirect("/user/login")

        request.setAttribute("users", UserRepository.getAll())
        return "/user/list.jsp"
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        throw NotFoundUrlException()
    }
}