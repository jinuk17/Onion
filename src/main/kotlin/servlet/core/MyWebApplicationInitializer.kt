package servlet.core

import mu.KotlinLogging
import servlet.core.mvc.DispatcherServlet
import javax.servlet.ServletContext

class MyWebApplicationInitializer : WebApplicationInitializer {

    private val logger = KotlinLogging.logger {}

    override fun onStartup(servletContext: ServletContext) {
        val dispatcher =
            servletContext.addServlet("dispatcher", DispatcherServlet("next"))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")
        logger.info("Start MyWebApplication Initializer")
    }
}