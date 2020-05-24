package servlet.core.mvc

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class ModelAndView(private val view: View) {

    private val model = mutableMapOf<String, Any>()

    fun addObject(attributeName: String, attributeValue: Any): ModelAndView {
        model[attributeName] = attributeValue
        return this
    }

    fun renderView(request: HttpServletRequest, response: HttpServletResponse) {
        view.render(model, request, response)
    }
}
