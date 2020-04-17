package servlet.app.dao

import servlet.core.db.ConnectionManger
import webserver.application.model.User
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        ConnectionManger.getConnection().use { conn ->
            val sql = "INSERT INTO users VALUES (?, ?, ?, ?)"
            conn.prepareStatement(sql).use {
                it.setString(1, user.id)
                it.setString(2, user.password)
                it.setString(3, user.name)
                it.setString(4, user.email)

                return it.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    fun findById(id: String): User?{
        ConnectionManger.getConnection().use { conn ->
            val sql = "SELECT userId, password, name, email FROM users WHERE userId = ?"
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.setString(1, id)
                pstmt.executeQuery().use { return user(it) }
            }
        }
    }

    @Throws(SQLException::class)
    fun findAll(): List<User> {
        ConnectionManger.getConnection().use { conn ->
            val sql = "SELECT userId, password, name, email FROM users"
            conn.prepareStatement(sql).use { pstmt ->
                pstmt.executeQuery().use { rs ->
                    return generateSequence { if(rs.next()) user(rs) else null }.toList()
                }
            }
        }
    }

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        ConnectionManger.getConnection().use { conn ->
            val sql = "UPDATE users set password = ?, name = ?, email = ? WHERE userId = ?"
            conn.prepareStatement(sql).use {
                it.setString(1, user.password)
                it.setString(2, user.name)
                it.setString(3, user.email)
                it.setString(4, user.id)
                return it.executeUpdate()
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