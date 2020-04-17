package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    private val jdbcTemplate = JdbcTemplate()
    private val selectJdbcTemplate = SelectJdbcTemplate()

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
        return selectJdbcTemplate.queryForObject(
            "SELECT userId, password, name, email FROM users WHERE userId = ?",
            {pstmt : PreparedStatement -> pstmt.setString(1, id) },
            {rs : ResultSet -> user(rs) }
        ).takeIf { it is User }.let { it as User }
    }

    @Throws(SQLException::class)
    fun findAll(): List<User> {
        return selectJdbcTemplate.query(
            "SELECT userId, password, name, email FROM users",
            { },
            {rs : ResultSet -> user(rs) }
        ).filterIsInstance<User>()
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