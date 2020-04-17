package servlet.app.dao

import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    private val jdbcTemplate = JdbcTemplate()

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        return jdbcTemplate.update("INSERT INTO users VALUES (?, ?, ?, ?)") {
            it.setString(1, user.id)
            it.setString(2, user.password)
            it.setString(3, user.name)
            it.setString(4, user.email)
        }
    }

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        return jdbcTemplate.update("UPDATE users set password = ?, name = ?, email = ? WHERE userId = ?") {
            it.setString(1, user.password)
            it.setString(2, user.name)
            it.setString(3, user.email)
            it.setString(4, user.id)
        }
    }

    @Throws(SQLException::class)
    fun findById(id: String): User?{
        return jdbcTemplate.queryForObject(
            "SELECT userId, password, name, email FROM users WHERE userId = ?",
            {pstmt : PreparedStatement -> pstmt.setString(1, id) },
            {rs : ResultSet -> user(rs) }
        )
    }

    @Throws(SQLException::class)
    fun findAll(): List<User> {
        return jdbcTemplate.query(
            "SELECT userId, password, name, email FROM users",
            { },
            {rs : ResultSet -> user(rs) }
        )
    }

    private fun user(rs: ResultSet): User {
        return User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
        )
    }
}