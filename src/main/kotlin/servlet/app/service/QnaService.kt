package servlet.app.service

import servlet.app.NotFoundResouce
import servlet.app.dao.AnswerDao
import servlet.app.dao.QuestionDao
import servlet.app.model.Answer
import servlet.app.model.Question
import servlet.app.model.Question.Companion.canDelete
import servlet.app.model.User


class QnaService(private val questionDao: QuestionDao, private val answerDao: AnswerDao) {

    fun findById(questionId: Long): Question? {
        return questionDao.findById(questionId)
    }

    fun findAllByQuestionId(questionId: Long): List<Answer?>? {
        return answerDao.findAllByQuestionId(questionId)
    }

    fun deleteQuestion(questionId: Long, user: User) {
        val question = questionDao.findById(questionId) ?: throw NotFoundResouce("Not Found Question, $questionId")

        val answers = answerDao.findAllByQuestionId(questionId)
        if(question.canDelete(user, answers)) {
            questionDao.delete(questionId)
        }
    }
}