package servlet.reflection

import mu.KotlinLogging
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import servlet.app.model.Question
import servlet.app.model.User
import servlet.app.model.UserId
import java.lang.reflect.Constructor

class ReflectionTest {
    private val logger = KotlinLogging.logger {}

    @Test
    fun showClass() {
        val clazz: Class<Question> = Question::class.java
        logger.info(clazz.name)
    }

    @Test
    @Throws(Exception::class)
    fun constructor() {
        val clazz: Class<Question> = Question::class.java
        val constructors: Array<Constructor<*>> = clazz.constructors
        for (constructor in constructors) {
            val parameterTypes: Array<Class<*>> = constructor.getParameterTypes()
            logger.info("paramer length : {}", parameterTypes.size)
            for (paramType in parameterTypes) {
                logger.info("param type : {}", paramType)
            }
        }
    }

    @Test
    fun newInstanceWithConstructorArg() {
        val userId = "userId"
        val userPassword = "password"
        val userName = "name"
        val userEmail = "email"

        val clazz = User::class.java
        val declaredConstructor =
            clazz.getDeclaredConstructor(UserId::class.java, String::class.java, String::class.java, String::class.java)

        val newInstance = declaredConstructor.newInstance(userId, userPassword, userName, userEmail)

        Assertions.assertEquals(newInstance, User(userId, userPassword, userName, userEmail))
    }

    @Test
    fun privateFieldAccess() {
        val clazz = Student::class.java
        val student = clazz.newInstance()
        val nameField = clazz.getDeclaredField("name")
        nameField.isAccessible = true
        nameField.set(student, "name")

        val ageField = clazz.getDeclaredField("age")
        ageField.isAccessible = true
        ageField.set(student, 10)

        Assertions.assertEquals(student.getName(), "name")
        Assertions.assertEquals(student.getAge(), 10)
    }
}