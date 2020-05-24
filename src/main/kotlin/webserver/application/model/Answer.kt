package webserver.application.model

import java.time.LocalDateTime


data class Answer (
    val answerId: Long,
    val writer: String,
    val contents: String,
    val createdDate: LocalDateTime,
    val questionId: Long
)

data class CreateAnswer (
    val writer: String,
    val contents: String,
    val questionId: Long
)