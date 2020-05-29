package servlet.core

import javax.servlet.ServletContainerInitializer
import javax.servlet.ServletContext
import javax.servlet.ServletException
import javax.servlet.annotation.HandlesTypes

@HandlesTypes(WebApplicationInitializer::class)
class MyServletContainerInitializer : ServletContainerInitializer {
    override fun onStartup(webApplicationInitializerClasses: Set<Class<*>>?, ctx: ServletContext) {

        val initializers= webApplicationInitializerClasses.orEmpty().map {
            try {
                it.newInstance() as WebApplicationInitializer
            } catch (e: Throwable) {
                throw ServletException("Failed to instantiate WebApplicationInitializer class", e)
            }
        }

        if (initializers.isEmpty()) {
            ctx.log("No Spring WebApplicationInitializer types detected on classpath")
            return
        }

        initializers.forEach { it.onStartup(ctx) }
    }
}