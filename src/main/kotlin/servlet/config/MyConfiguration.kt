package servlet.config

import com.zaxxer.hikari.HikariDataSource
import servlet.app.dao.JdbcTemplate
import servlet.core.annotation.Bean
import servlet.core.annotation.ComponentScan
import servlet.core.annotation.Configuration
import javax.sql.DataSource

@Configuration
@ComponentScan(["next", "core"])
class MyConfiguration {

    @Bean
    fun dataSource(): DataSource {
        val ds = HikariDataSource()
        ds.driverClassName = "com.mysql.jdbc.Driver"
        ds.jdbcUrl = "jdbc:mysql://localhost:3306/onion"
        ds.username = "admin"
        ds.password = "admin"

        ds.addDataSourceProperty("useSSL", false)
        ds.addDataSourceProperty("characterEncoding","utf8")
        ds.addDataSourceProperty("useUnicode","true")
        return ds
    }

    @Bean
    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
        return JdbcTemplate(dataSource)
    }
}