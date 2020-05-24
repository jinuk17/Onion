package servlet.core

import servlet.app.controller.*

class RequestMapping {

    private val urlMappings: MutableMap<String, Controller> = mutableMapOf()
    init {
        addController("/hello", HelloWorldController())
        addController("/user/login", LoginController())
        addController("/user/logout", LogoutController())
        addController("/user/create", UserCreateController())
        addController("/user/list", UserListController())
        addController("/user/update", UserUpdateController())
        addController("/api/qna/answer", AddAnswerController())
    }

    fun getController(url: String): Controller? {
        return urlMappings[url]
    }

    fun addController(url: String, controller: Controller) {
        urlMappings[url] = controller
    }
}