package servlet.controller

import servlet.Controller
import servlet.Controller.Companion.redirect
import webserver.application.model.User
import webserver.application.repository.UserRepository
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class UserCreateController: Controller {

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

        UserRepository.save(user)

        return redirect("/user/list")
    }
}