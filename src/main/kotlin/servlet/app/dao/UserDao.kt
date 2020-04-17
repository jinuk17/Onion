package servlet.app.dao

import servlet.core.db.ConnectionManger
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException


class UserDao {

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        ConnectionManger.getConnection().use { conn ->
            val sql = createQueryForInsert()
            conn.prepareStatement(sql).use {
                setValuesForInsert(it, user)
                return it.executeUpdate()
            }
        }
    }

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        ConnectionManger.getConnection().use { conn ->
            val sql = createQueryForUpdate()
            conn.prepareStatement(sql).use {
                setValuesForUpdate(it, user)
                return it.executeUpdate()
            }
        }
    }

    private fun createQueryForInsert() = "INSERT INTO users VALUES (?, ?, ?, ?)"
    private fun createQueryForUpdate() = "UPDATE users set password = ?, name = ?, email = ? WHERE userId = ?"

    private fun setValuesForInsert(it: PreparedStatement, user: User) {
        it.setString(1, user.id)
        it.setString(2, user.password)
        it.setString(3, user.name)
        it.setString(4, user.email)
    }

    private fun setValuesForUpdate(it: PreparedStatement, user: User) {
        it.setString(1, user.password)
        it.setString(2, user.name)
        it.setString(3, user.email)
        it.setString(4, user.id)
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

    private fun user(rs: ResultSet): User {
        return User(
            rs.getString("userId"),
            rs.getString("password"),
            rs.getString("name"),
            rs.getString("email")
        )
    }
}