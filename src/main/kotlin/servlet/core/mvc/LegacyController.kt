package servlet.core.mvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


interface LegacyController {
    fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView
    fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView

    companion object {
        fun jspView(forwardUrl: String): ModelAndView {
            return ModelAndView(JspView(forwardUrl))
        }

        fun jsonView(): ModelAndView {
            return ModelAndView(JsonView())
        }

        fun redirectView(redirectUrl: String): ModelAndView {
            return ModelAndView(RedirectView(redirectUrl))
        }
    }
}