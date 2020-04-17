package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.PreparedStatement
import java.sql.SQLException

abstract class InsertJdbcTemplate {

    @Throws(SQLException::class)
    fun insert(user: User): Int? {
        ConnectionManager.getConnection().use { conn ->
            val sql = createQueryForInsert()
            conn.prepareStatement(sql).use {
                setValuesForInsert(it, user)
                return it.executeUpdate()
            }
        }
    }

    abstract fun setValuesForInsert(pstmt: PreparedStatement, user: User)
    abstract fun createQueryForInsert(): String
}