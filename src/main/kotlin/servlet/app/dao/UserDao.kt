package servlet.app.dao

import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    private val jdbcTemplate = JdbcTemplate()

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        return jdbcTemplate.update(
            "INSERT INTO users VALUES (?, ?, ?, ?)",
            user.id,
            user.password,
            user.name,
            user.email
        )
    }

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        return jdbcTemplate.update(
            "UPDATE users set password = ?, name = ?, email = ? WHERE userId = ?",
            user.password,
            user.name,
            user.email,
            user.id
        )
    }

    @Throws(SQLException::class)
    fun findById(id: String): User?{
        return jdbcTemplate.queryForObject(
            "SELECT userId, password, name, email FROM users WHERE userId = ?", id) { user(it) }
    }

    @Throws(SQLException::class)
    fun findAll(): List<User> {
        return jdbcTemplate.query("SELECT userId, password, name, email FROM users") { user(it) }
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