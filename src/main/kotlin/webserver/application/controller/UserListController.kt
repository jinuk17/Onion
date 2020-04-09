package webserver.application.controller

import webserver.application.repository.UserRepository
import webserver.framework.controller.AbstractController
import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse
import webserver.framework.util.HttpRequestParserUtil


class UserListController: AbstractController() {

    override fun doGet(request: HttpRequest, response: HttpResponse) {
        if(checkAuthorized(request)) {
            val users =
                UserRepository.getAll().joinToString("<br/>") { "<h3>$it</h3>" }
            response.forwardBody(users.toByteArray())
        }else{
            response.redirect("/user/login.html")
        }
    }

    private fun checkAuthorized(request: HttpRequest): Boolean {
        return request.getHeader("Cookie")?.let {
            HttpRequestParserUtil.parseQueryParameters(it, ";", "=")
        }?.get("logined")?.toBoolean() ?: false
    }
}