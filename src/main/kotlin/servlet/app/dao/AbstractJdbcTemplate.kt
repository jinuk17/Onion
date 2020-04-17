package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.SQLException

abstract class AbstractJdbcTemplate {

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        ConnectionManager.getConnection().use { conn ->
            val sql = createQuery()
            conn.prepareStatement(sql).use {
                setValues(it, user)
                return it.executeUpdate()
            }
        }
    }

    abstract fun setValues(pstmt: PreparedStatement, user: User)
    abstract fun createQuery(): String
}