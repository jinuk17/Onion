package servlet.app.dao

import servlet.app.dao.DateExtensions.toLocalDateTime
import servlet.app.model.Answer
import servlet.app.model.CreateAnswer
import servlet.core.annotation.Inject
import servlet.core.annotation.Repository
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.ZoneId

@Repository
class AnswerDao @Inject constructor(private val jdbcTemplate: JdbcTemplate) {

    fun insert(create: CreateAnswer): Answer? {
        val keyHolder = KeyHolder()
        jdbcTemplate.update(
            "INSERT INTO answers (writer, contents, createdDate, questionId) VALUES (?, ?, ?, ?)",
            create.writer,
            create.contents,
            LocalDateTime.now(),
            create.questionId,
            keyHolder = keyHolder
        )

        return keyHolder.id?.let { findById(it) }
    }

    @Throws(SQLException::class)
    fun findById(id: Long): Answer? {
        return jdbcTemplate.queryForObject(
            "SELECT answerId, writer, contents, createdDate, questionId FROM answers WHERE answerId = ?", id
        ) { answer(it) }
    }

    fun findAllByQuestionId(questionId: Long): List<Answer> {
        val sql = ("SELECT answerId, questionId, writer, contents, createdDate FROM ANSWERS " +
                "WHERE questionId = ? " +
                "order by answerId desc")

        return jdbcTemplate.query(sql, questionId) {
            Answer(
                it.getLong("answerId"),
                it.getString("writer"),
                it.getString("contents"),
                it.getTimestamp("createdDate").toLocalDateTime(),
                it.getLong("questionId")
            )
        }
    }

    private fun answer(rs: ResultSet): Answer {
        return Answer(
            rs.getLong("answerId"),
            rs.getString("writer"),
            rs.getString("contents"),
            rs.getDate("createdDate").toLocalDateTime(ZoneId.systemDefault()),
            rs.getLong("questionId")
        )
    }
}
