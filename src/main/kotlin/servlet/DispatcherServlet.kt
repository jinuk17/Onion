package servlet

import javax.servlet.ServletException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "dispatcher", urlPatterns = ["/"], loadOnStartup = 1)
class DispatcherServlet: HttpServlet() {

    private lateinit var requestMapping: RequestMapping

    override fun init() {
        requestMapping = RequestMapping()
    }

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {

        val controller = requestMapping.getController(req.requestURI) ?: throw NotFoundUrlException()

        val viewName = if(req.method == "POST") controller.post(req, resp) else controller.get(req, resp)
        move(viewName, req, resp)

    }

    private fun move(
        viewName: String,
        req: HttpServletRequest,
        resp: HttpServletResponse
    ) {
        if (viewName.startsWith(REDIRECT_PREFIX)) {
            resp.sendRedirect(viewName.substring(REDIRECT_PREFIX.length))
            return
        }
        req.getRequestDispatcher(viewName).forward(req, resp)
    }

    companion object {
        internal const val REDIRECT_PREFIX = "redirect:"
    }
}