package servlet.core

import mu.KotlinLogging
import servlet.core.mvc.AnnotationHandlerMapping
import servlet.core.mvc.DispatcherServlet
import javax.servlet.ServletContext

class MyWebApplicationInitializer : WebApplicationInitializer {

    private val logger = KotlinLogging.logger {}

    override fun onStartup(servletContext: ServletContext) {

        val handlerMapping = AnnotationHandlerMapping("next")
        val dispatcher =
            servletContext.addServlet("dispatcher", DispatcherServlet(handlerMapping))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")
        logger.info("Start MyWebApplication Initializer")
    }
}