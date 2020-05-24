package servlet.core.mvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface View {
    @Throws(Exception::class)
    fun render(model: Map<String, Any>, request: HttpServletRequest, response: HttpServletResponse)
}