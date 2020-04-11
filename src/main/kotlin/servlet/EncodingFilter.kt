package servlet

import javax.servlet.*
import javax.servlet.annotation.WebFilter

@WebFilter("/*")
class EncodingFilter: Filter {
    override fun destroy() {}

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        request.characterEncoding = "UTF-8"
        chain.doFilter(request, response) //sends request to next resource
    }

    override fun init(filterConfig: FilterConfig?) {}
}