package servlet.core

import javax.servlet.ServletContext

interface WebApplicationInitializer {
    fun onStartup(servletContext: ServletContext)
}