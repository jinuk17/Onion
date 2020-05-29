package servlet.app.dao

import servlet.core.annotation.Component
import servlet.core.db.ConnectionManager
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Statement
import javax.sql.DataSource

@Component
class JdbcTemplate(private val dataSource: DataSource) {

    @Throws(DataAccessException::class)
    fun update(sql: String, vararg values: Any, keyHolder: KeyHolder? = null): Int {
        try {
            ConnectionManager.getConnection().use { conn ->
                conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS).use { pstmt ->
                    values.forEachIndexed{ i, v -> pstmt.setObject(i+1, v) }
                    val count = pstmt.executeUpdate()

                    keyHolder?.let { kh ->
                        pstmt.generatedKeys.use {
                            if (it.next()) {
                                kh.id = it.getLong(1)
                            }
                        }
                    }

                    return count
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