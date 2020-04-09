package webserver.conf

import webserver.application.controller.LoginController
import webserver.application.controller.UserCreateController
import webserver.application.controller.UserListController
import webserver.framework.controller.Controller

object RequestMapping {

    private val controllers: Map<String, Controller>
    init {
        controllers = mapOf(
            Pair("/user/create", UserCreateController()),
            Pair("/user/login", LoginController()),
            Pair("/user/list", UserListController())
        )
    }

    fun getController(path: String): Controller? {
        return controllers[path]
    }
}
