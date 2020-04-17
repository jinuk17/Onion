package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.SQLException

class UpdateJdbcTemplate {

    @Throws(SQLException::class)
    fun update(user: User, userDao: UserDao): Int? {
        ConnectionManager.getConnection().use { conn ->
            val sql = userDao.createQueryForUpdate()
            conn.prepareStatement(sql).use {
                userDao.setValuesForUpdate(it, user)
                return it.executeUpdate()
            }
        }
    }
}