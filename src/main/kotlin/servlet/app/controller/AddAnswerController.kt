package servlet.app.controller

import mu.KotlinLogging
import servlet.app.dao.AnswerDao
import servlet.core.mvc.LegacyController
import servlet.core.NotFoundUrlException
import servlet.core.mvc.LegacyController.Companion.jsonView
import servlet.core.mvc.ModelAndView
import servlet.app.model.CreateAnswer
import servlet.core.annotation.Controller
import servlet.core.annotation.Inject
import java.lang.IllegalStateException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class AddAnswerController @Inject constructor(private val answerDao: AnswerDao): LegacyController {

    private val logger = KotlinLogging.logger {}

    override fun get(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        throw NotFoundUrlException()
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        val create = CreateAnswer(
            request.getParameter("writer"),
            request.getParameter("contents"),
            request.getParameter("questionId").toLong()
        )

        logger.info { "answer : $create" }
        val answer = answerDao.insert(create)?: throw IllegalStateException("failed user creation.")
        return jsonView().addObject("answer", answer)
    }

}