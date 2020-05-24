package servlet.core.mvc

import mu.KotlinLogging
import servlet.core.NotFoundUrlException
import servlet.core.RequestMapping
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name = "dispatcher", urlPatterns = ["/"], loadOnStartup = 1)
class DispatcherServlet: HttpServlet() {

    private val logger = KotlinLogging.logger {}
    private lateinit var requestMapping: RequestMapping

    override fun init() {
        requestMapping = RequestMapping()
    }

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {

        logger.info { "request url : ${req.requestURI}" }

        val controller = requestMapping.getController(req.requestURI) ?: throw NotFoundUrlException()

        val modelAndView = if(req.method == "POST") controller.post(req, resp) else controller.get(req, resp)
        modelAndView.renderView(req, resp)
    }
}