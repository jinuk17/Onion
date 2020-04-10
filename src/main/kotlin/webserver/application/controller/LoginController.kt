package webserver.application.controller

import webserver.application.model.Login
import webserver.application.repository.UserRepository
import webserver.framework.controller.AbstractController
import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse
import java.lang.IllegalArgumentException

class LoginController: AbstractController() {

    override fun doPost(request: HttpRequest, response: HttpResponse) {

        val login = parseLogin(request)
        val loginUser = UserRepository.get(login.userId)?.takeIf { it.password == login.password }

        if(loginUser != null) {
            val session = request.getSession()
            session.setAttribute("user", loginUser)
            response.redirect("/index.html")
        }else{
            response.redirect("/user/login_failed.html")
        }
    }

    private fun parseLogin(request: HttpRequest): Login {
        return request.getParameter("userId")?.let { userId ->
        request.getParameter("password")?.let { password ->
            Login(userId, password)
        }} ?: throw IllegalArgumentException("Invalid Login Info : ${request.getParameter()}")
    }
}