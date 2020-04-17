package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
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
        ConnectionManager.getConnection().use { conn ->
            val sql = "SELECT userId, password, name, email FROM users WHERE userId = ?"
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, id)
                pstmt.executeQuery().use { return if(it.next()) user(it) else null }
            }
        }
    }

    @Throws(SQLException::class)
    fun findAll(): List<User> {
        ConnectionManager.getConnection().use { conn ->
            val sql = "SELECT userId, password, name, email FROM users"
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.executeQuery().use { rs ->
                    return generateSequence { if(rs.next()) user(rs) else null }.toList()
                }
            }
        }
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