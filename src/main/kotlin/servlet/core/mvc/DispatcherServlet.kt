package servlet.core.mvc

import mu.KotlinLogging
import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DispatcherServlet(private val handlerMapping: HandlerMapping) : HttpServlet() {

    private val logger = KotlinLogging.logger {}
    private val mappings: MutableList<HandlerMapping> = mutableListOf()
    private val handlerAdapters: MutableList<HandlerAdapter> = mutableListOf()

    override fun init() {
        mappings.add(handlerMapping)

        handlerAdapters.add(ControllerHandlerAdapter())
        handlerAdapters.add(HandlerExecutionHandlerAdapter())
    }

    override fun service(req: HttpServletRequest, resp: HttpServletResponse) {

        val handler: Any = getHandler(req) ?: throw IllegalArgumentException("Not Found Url")

        try {
            val mav: ModelAndView = execute(handler, req, resp)
            mav.renderView(req, resp)
        } catch (e: Throwable) {
            logger.error("Exception : $e", e)
            throw ServletException(e.message)
        }
    }


    /*
    * https://kotlinlang.org/docs/reference/sequences.html#sequences
    * */
    private fun getHandler(req: HttpServletRequest): Any? {
        return mappings.asSequence().map { it.getHandler(req) }.find { it != null }
    }

    private fun execute(handler: Any, req: HttpServletRequest, resp: HttpServletResponse): ModelAndView {
        return handlerAdapters.asSequence().filter { it.supports(handler) }.map { it.handle(req, resp, handler)  }.first()
    }
}