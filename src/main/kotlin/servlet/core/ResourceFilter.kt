package servlet.core

import mu.KotlinLogging
import javax.servlet.*
import javax.servlet.annotation.WebFilter
import javax.servlet.http.HttpServletRequest

@WebFilter("/*")
class ResourceFilter: Filter {

    private val logger = KotlinLogging.logger {}
    private lateinit var defaultRequestDispatcher: RequestDispatcher

    override fun init(filterConfig: FilterConfig) {
        defaultRequestDispatcher = filterConfig.servletContext.getNamedDispatcher("default")
    }

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val path = req.requestURI.substring(req.contextPath.length)
        if(isResourceUrl(path)) {
            logger.debug { "path : $path" }
            defaultRequestDispatcher.forward(request, response)
        }else{
            chain.doFilter(request, response)
        }
    }

    override fun destroy() {}

    private fun isResourceUrl(path: String): Boolean {
        return resourcePrefixes.any { path.startsWith(it) }
    }

    companion object {
        val resourcePrefixes = listOf("/css", "/js", "/fronts", "/images", "/favicon.ico")
    }
}