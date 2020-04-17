package servlet.app.dao

import servlet.core.db.ConnectionManager
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException

class JdbcTemplate {

    @Throws(DataAccessException::class)
    fun update(sql: String, setValues: (PreparedStatement) -> Unit): Int? {
        try {
            ConnectionManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use {
                    setValues(it)
                    return it.executeUpdate()
                }
            }
        }catch (e: SQLException) {
            throw DataAccessException(e)
        }

    }

    @Throws(DataAccessException::class)
    fun <T> query(sql: String, setValues: (PreparedStatement) -> Unit, mapRow: (ResultSet) -> T): List<T> {
        try{
            ConnectionManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    setValues(pstmt)
                    pstmt.executeQuery().use {
                        return generateSequence { if(it.next()) mapRow(it) else null }.toList()
                    }
                }
            }
        }catch (e: SQLException) {
            throw DataAccessException(e)
        }
    }

    @Throws(DataAccessException::class)
    fun <T> queryForObject(sql: String, setValues: (PreparedStatement) -> Unit, mapRow: (ResultSet) -> T): T? =
        query(sql, setValues, mapRow).firstOrNull()
}