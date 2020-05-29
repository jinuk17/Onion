package servlet.app.repository

import org.springframework.jdbc.core.JdbcTemplate
import servlet.core.annotation.Inject
import servlet.core.annotation.Repository

@Repository
class JdbcUserRepository @Inject constructor(
    private val jdbcTemplate: JdbcTemplate
) : UserRepository
