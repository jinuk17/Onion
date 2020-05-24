package servlet.app.controller

import mu.KotlinLogging
import servlet.app.dao.AnswerDao
import servlet.core.Controller
import servlet.core.NotFoundUrlException
import servlet.core.OnionObjectMapper
import webserver.application.model.CreateAnswer
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AddAnswerController : Controller {

    private val logger = KotlinLogging.logger {}

    private val answerDao = AnswerDao()
    private val mapper = OnionObjectMapper.mapper

    override fun get(request: HttpServletRequest, response: HttpServletResponse): String {
        throw NotFoundUrlException()
    }

    override fun post(request: HttpServletRequest, response: HttpServletResponse): String {

        val create = CreateAnswer(
            request.getParameter("writer"),
            request.getParameter("contents"),
            request.getParameter("questionId").toLong()
        )

        logger.info { "answer : $create" }

        val answer = answerDao.insert(create)

        response.contentType = ("application/json;charset=UTF-8")
        val out = response.writer

        answer?.let {
            out.print(mapper.writeValueAsString(it))
        }
        return ""
    }

}