package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.SQLException

abstract class AbstractJdbcTemplate {

    @Throws(SQLException::class)
    fun update(sql: String): Int? {
        ConnectionManager.getConnection().use { conn ->
            conn.prepareStatement(sql).use {
                setValues(it)
                return it.executeUpdate()
            }
        }
    }

    abstract fun setValues(pstmt: PreparedStatement)
}