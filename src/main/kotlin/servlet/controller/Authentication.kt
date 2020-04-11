package servlet.controller

import webserver.application.model.User
import javax.servlet.http.HttpServletRequest


object Authentication {

    fun getSessionUser(req: HttpServletRequest): User? {
        return req.session.getAttribute("user")?.takeIf { it is User }?.let { it as User }
    }

    fun hasUserSession(req: HttpServletRequest): Boolean {
        return getSessionUser(req) != null
    }
}