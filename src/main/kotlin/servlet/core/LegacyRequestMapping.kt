package servlet.core

import servlet.app.controller.*
import servlet.core.mvc.Controller
import servlet.core.mvc.HandlerMapping
import javax.servlet.http.HttpServletRequest

class LegacyRequestMapping : HandlerMapping {

    private val urlMappings: MutableMap<String, Controller> = mutableMapOf()

    init {
        addController("/user/login", LoginController())
        addController("/user/logout", LogoutController())
        addController("/user/create", UserCreateController())
        addController("/user/list", UserListController())
        addController("/user/update", UserUpdateController())
        addController("/api/qna/answer", AddAnswerController())
    }

    override fun getHandler(request: HttpServletRequest): Controller? {
        return urlMappings[request.requestURI]
    }

    private fun addController(url: String, controller: Controller) {
        urlMappings[url] = controller
    }
}