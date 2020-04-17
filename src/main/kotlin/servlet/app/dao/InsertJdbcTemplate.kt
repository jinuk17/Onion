package servlet.app.dao

import servlet.core.db.ConnectionManager
import webserver.application.model.User
import java.sql.SQLException

class InsertJdbcTemplate {

    @Throws(SQLException::class)
    fun insert(user: User, userDao: UserDao): Int? {
        ConnectionManager.getConnection().use { conn ->
            val sql = userDao.createQueryForInsert()
            conn.prepareStatement(sql).use {
                userDao.setValuesForInsert(it, user)
                return it.executeUpdate()
            }
        }
    }
}