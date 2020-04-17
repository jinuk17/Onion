package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        val jdbcTemplate = InsertJdbcTemplate()
        return jdbcTemplate.insert(user, this)
    }

    fun createQueryForInsert() = "INSERT INTO users VALUES (?, ?, ?, ?)"
    fun setValuesForInsert(it: PreparedStatement, user: User) {
        it.setString(1, user.id)
        it.setString(2, user.password)
        it.setString(3, user.name)
        it.setString(4, user.email)
    }

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        val jdbcTemplate = UpdateJdbcTemplate()
        return jdbcTemplate.update(user, this)
    }

    fun createQueryForUpdate() = "UPDATE users set password = ?, name = ?, email = ? WHERE userId = ?"
    fun setValuesForUpdate(it: PreparedStatement, user: User) {
        it.setString(1, user.password)
        it.setString(2, user.name)
        it.setString(3, user.email)
        it.setString(4, user.id)
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