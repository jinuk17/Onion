package servlet.app.dao

import servlet.core.db.ConnectionManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class SelectJdbcTemplate {

    @Throws(SQLException::class)
    fun query(sql: String, setValues: (PreparedStatement) -> Unit, mapRow: (ResultSet) -> Any): List<Any> {
        ConnectionManager.getConnection().use { conn ->
            conn.prepareStatement(sql).use { pstmt ->
                setValues(pstmt)
                pstmt.executeQuery().use {
                    return generateSequence { if(it.next()) mapRow(it) else null }.toList()
                }
            }
        }
    }

    @Throws(SQLException::class)
    fun queryForObject(sql: String, setValues: (PreparedStatement) -> Unit, mapRow: (ResultSet) -> Any): Any? =
        query(sql, setValues, mapRow).firstOrNull()
}