package servlet.app.controller

import mu.KotlinLogging
import servlet.app.dao.AnswerDao
import servlet.core.mvc.Controller
import servlet.core.NotFoundUrlException
import servlet.core.mvc.Controller.Companion.jsonView
import servlet.core.mvc.ModelAndView
import servlet.app.model.CreateAnswer
import java.lang.IllegalStateException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AddAnswerController : Controller {

    private val logger = KotlinLogging.logger {}

    private val answerDao = AnswerDao()

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