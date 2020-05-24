package servlet.core.mvc

import servlet.core.OnionObjectMapper
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JsonView : View {

    override fun render(model: Map<String, Any>, request: HttpServletRequest, response: HttpServletResponse) {

        val mapper = OnionObjectMapper.mapper
        response.contentType = "application/json;charset=UTF-8"
        val out = response.writer
        out.print(mapper.writeValueAsString(model))
    }
}