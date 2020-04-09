package webserver.application.controller

import webserver.application.model.User
import webserver.application.repository.UserRepository
import webserver.framework.controller.AbstractController
import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse
import java.lang.IllegalArgumentException


class UserCreateController: AbstractController() {

    override fun doPost(request: HttpRequest, response: HttpResponse) {
        val user = handleUserCreate(request)
        if(user != null) {
            response.redirect("/index.html")
        }
    }

    private fun handleUserCreate(request: HttpRequest): User? {

        val user =
            request.getParameter("userId")?.let { userId ->
                request.getParameter("password")?.let { password ->
                    request.getParameter("name")?.let { name ->
                        request.getParameter("email")?.let { email ->
                            User(userId, password, name, email)
                        }
                    }
                }
            } ?: throw IllegalArgumentException("Invalid User Info : ${request.getParameter()}")

        return UserRepository.save(user)
    }

}