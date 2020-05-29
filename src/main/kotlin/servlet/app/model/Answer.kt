package servlet.app.model

import servlet.app.model.Question.Companion.canDelete
import java.lang.IllegalArgumentException
import java.time.LocalDateTime


data class Answer (
    val answerId: Long,
    val writer: String,
    val contents: String,
    val createdDate: LocalDateTime,
    val questionId: Long
){
    companion object {
        fun Answer.canDelete(user: User): Boolean {
            if(user.name != this.writer) throw IllegalArgumentException("Cannot delete answers written by other users.")
            else return true
        }
    }
}

data class CreateAnswer (
    val writer: String,
    val contents: String,
    val questionId: Long
)