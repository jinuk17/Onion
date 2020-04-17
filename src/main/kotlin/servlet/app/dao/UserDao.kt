package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        val jdbcTemplate = object : AbstractJdbcTemplate() {
            override fun setValues(pstmt: PreparedStatement, user: User) {
                pstmt.setString(1, user.id)
                pstmt.setString(2, user.password)
                pstmt.setString(3, user.name)
                pstmt.setString(4, user.email)
            }
            override fun createQuery() = "INSERT INTO users VALUES (?, ?, ?, ?)"
        }
        return jdbcTemplate.update(user)
    }



    @Throws(SQLException::class)
    fun update(user: User): Int? {
        val jdbcTemplate = object : AbstractJdbcTemplate() {
            override fun setValues(pstmt: PreparedStatement, user: User) {
                pstmt.setString(1, user.password)
                pstmt.setString(2, user.name)
                pstmt.setString(3, user.email)
                pstmt.setString(4, user.id)
            }

            override fun createQuery() = "UPDATE users set password = ?, name = ?, email = ? WHERE userId = ?"

        }
        return jdbcTemplate.update(user)
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