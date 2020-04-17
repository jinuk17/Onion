package servlet.app.dao

import servlet.core.db.ConnectionManager
import java.sql.PreparedStatement
import java.sql.SQLException

class JdbcTemplate {

    @Throws(SQLException::class)
    fun update(sql: String, setValues: (PreparedStatement) -> Unit): Int? {
        ConnectionManager.getConnection().use { conn ->
            conn.prepareStatement(sql).use {
                setValues(it)
                return it.executeUpdate()
            }
        }
    }
}