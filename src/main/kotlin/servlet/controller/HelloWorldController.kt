package servlet.controller

import servlet.Controller
import servlet.NotFoundUrlException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class HelloWorldController: Controller {

    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        response.writer.write("Hello, World!")
        return ""
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {
        throw NotFoundUrlException()
    }
}