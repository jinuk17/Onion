package servlet.core

import mu.KotlinLogging
import servlet.config.MyConfiguration
import servlet.core.di.AnnotationConfigApplicationContext
import servlet.core.mvc.AnnotationHandlerMapping
import servlet.core.mvc.DispatcherServlet
import javax.servlet.ServletContext

class MyWebApplicationInitializer : WebApplicationInitializer {

    private val logger = KotlinLogging.logger {}

    override fun onStartup(servletContext: ServletContext) {

        val applicationContext = AnnotationConfigApplicationContext(MyConfiguration::class.java)

        val handlerMapping = AnnotationHandlerMapping(applicationContext)
        val dispatcher =
            servletContext.addServlet("dispatcher", DispatcherServlet(handlerMapping))
        dispatcher.setLoadOnStartup(1)
        dispatcher.addMapping("/")
        logger.info("Start MyWebApplication Initializer")
    }
}