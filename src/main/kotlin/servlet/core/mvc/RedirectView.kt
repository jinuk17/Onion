package servlet.core.mvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class RedirectView(private val viewName: String) : View {

    override fun render(model: Map<String, Any>, request: HttpServletRequest, response: HttpServletResponse) {
        model.forEach { (k, v) -> request.setAttribute(k, v) }
        response.sendRedirect(viewName)
    }
}