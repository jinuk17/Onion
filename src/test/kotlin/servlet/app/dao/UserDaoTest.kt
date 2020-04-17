package servlet.app.dao

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import servlet.core.db.ConnectionManager
import webserver.application.model.User


class UserDaoTest {

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            val populator = ResourceDatabasePopulator()
            populator.addScript(ClassPathResource("jwp.sql"))
            DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource())
        }
    }

    @Test
    @Throws(Exception::class)
    fun crud() {
        var expected = User("userId", "password", "name", "mail@email.com")
        val userDao = UserDao()
        userDao.insert(expected)
        var actual: User? = userDao.findById(expected.id)
        assertEquals(expected, actual)

        expected = expected.copy(
            id = "userId",
            password = "password2",
            name = "name2",
            email = "sanjigi@email.com"
        )
        userDao.update(expected)
        actual = userDao.findById(expected.id)
        assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun findAll() {
        val userDao = UserDao()
        val users: List<User> = userDao.findAll()
        assertEquals(5, users.size)
    }
}