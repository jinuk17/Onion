package servlet.app.dao

import webserver.application.model.Answer
import webserver.application.model.CreateAnswer
import webserver.application.model.User
import java.sql.Date
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

private fun Date.toLocalDateTime(zone: ZoneId): LocalDateTime {
    return Instant.ofEpochMilli(this.time)
        .atZone(zone)
        .toLocalDateTime()
}

class AnswerDao {

    private val jdbcTemplate = JdbcTemplate()

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
    fun findById(id: Long): Answer?{
        return jdbcTemplate.queryForObject(
            "SELECT answerId, writer, contents, createdDate, questionId FROM answers WHERE answerId = ?", id) { answer(it) }
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