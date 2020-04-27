package servlet.app.dao

import servlet.core.db.ConnectionManager
import java.sql.ResultSet
import java.sql.SQLException

class JdbcTemplate {

    @Throws(DataAccessException::class)
    fun update(sql: String, vararg values: Any): Int? {
        try {
            ConnectionManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    values.forEachIndexed{ i, v -> pstmt.setObject(i+1, v) }
                    return pstmt.executeUpdate()
                }
            }
        }catch (e: SQLException) {
            throw DataAccessException(e)
        }

    }

    @Throws(DataAccessException::class)
    fun <T: Any> query(sql: String, vararg values: Any, mapRow: (ResultSet) -> T): List<T> {
        try{
            ConnectionManager.getConnection().use { conn ->
                conn.prepareStatement(sql).use { pstmt ->
                    values.forEachIndexed{ i, v -> pstmt.setObject(i+1, v) }
                    pstmt.executeQuery().use {
                        return generateSequence { if (it.next()) mapRow(it) else null }.toList()
                    }
                }
            }
        }catch (e: SQLException) {
            throw DataAccessException(e)
        }
    }

    @Throws(DataAccessException::class)
    fun <T: Any> queryForObject(sql: String, vararg values: Any, mapRow: (ResultSet) -> T): T? =
        query(sql, *values, mapRow = mapRow).firstOrNull()
}