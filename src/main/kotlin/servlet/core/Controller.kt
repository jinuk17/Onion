package servlet.core

import servlet.core.DispatcherServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


interface Controller {
    fun get(request: HttpServletRequest, response: HttpServletResponse): String
    fun post(request: HttpServletRequest, response: HttpServletResponse): String
    companion object {
        fun redirect(viewName: String): String {
            return "${DispatcherServlet.REDIRECT_PREFIX}$viewName"
        }
    }
}