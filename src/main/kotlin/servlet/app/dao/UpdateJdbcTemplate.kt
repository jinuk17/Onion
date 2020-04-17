package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.SQLException

abstract class UpdateJdbcTemplate {

    @Throws(SQLException::class)
    fun update(user: User): Int? {
        ConnectionManager.getConnection().use { conn ->
            val sql = createQueryForUpdate()
            conn.prepareStatement(sql).use {
                setValuesForUpdate(it, user)
                return it.executeUpdate()
            }
        }
    }

    abstract fun setValuesForUpdate(pstmt: PreparedStatement, user: User)
    abstract fun createQueryForUpdate(): String
}