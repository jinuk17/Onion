package servlet.core.db

import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource


object ConnectionManger {
    private const val DB_DRIVER = "com.mysql.jdbc.Driver"
    private const val DB_URL = "jdbc:mysql://localhost:3306/onion"
    private const val DB_USERNAME = "admin"
    private const val DB_PW = "admin"

    fun getDataSource(): DataSource {
        val ds = HikariDataSource()
        ds.driverClassName = DB_DRIVER
        ds.jdbcUrl = DB_URL
        ds.username = DB_USERNAME
        ds.password = DB_PW

        ds.addDataSourceProperty("useSSL", false)
        ds.addDataSourceProperty("characterEncoding","utf8")
        ds.addDataSourceProperty("useUnicode","true")
        return ds
    }

    fun getConnection(): Connection {
        return try {
            getDataSource().connection
        } catch (e: SQLException) {
            throw IllegalStateException(e)
        }
    }
}