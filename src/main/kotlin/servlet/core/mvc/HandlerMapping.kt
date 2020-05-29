package servlet.core.mvc

import javax.servlet.http.HttpServletRequest

interface HandlerMapping {
    fun getHandler(request: HttpServletRequest): Any?
}