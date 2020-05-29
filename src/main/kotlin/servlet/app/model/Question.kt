package servlet.app.model

import servlet.app.model.Answer.Companion.canDelete
import java.lang.IllegalArgumentException
import java.time.LocalDateTime

data class Question(
    val questionId: Long,
    val writer: String,
    val title: String,
    val contents: String,
    val countOfAnswer: Int,
    val createdDate: LocalDateTime
){

    companion object {
        fun Question.canDelete(user: User, answers: List<Answer>): Boolean {

            if(user.name != this.writer) {
                throw IllegalArgumentException("Cannot delete questions written by other users.")
            }
            
            return answers.all { it.canDelete(user) }
        }
    }

}


