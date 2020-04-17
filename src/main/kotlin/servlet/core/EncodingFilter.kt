package servlet.core

import javax.servlet.*
import javax.servlet.annotation.WebFilter

@WebFilter("/*")
class EncodingFilter: Filter {

    override fun init(filterConfig: FilterConfig?) {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request.characterEncoding = "UTF-8"
        chain.doFilter(request, response) //sends request to next resource
    }

    override fun destroy() {}
}