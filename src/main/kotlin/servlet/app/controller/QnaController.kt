package servlet.app.controller

import servlet.app.service.MyQnaService
import servlet.core.annotation.Controller
import servlet.core.annotation.Inject
import servlet.core.annotation.RequestMapping
import servlet.core.mvc.Controller.Companion.jspView
import servlet.core.mvc.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class QnaController @Inject constructor(private val qnaService: MyQnaService) {

    @RequestMapping("/questions")
    fun list(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView("/qna/list.jsp")
    }
}