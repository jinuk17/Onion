package webserver

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.io.*

class HttpResponseTest {

    companion object {

        private const val testDirectory = "./src/test/resources/http/response/"

        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            File(testDirectory).deleteRecursively()
            File(testDirectory).mkdirs()
        }

        @AfterAll
        @JvmStatic
        internal fun afterAll() {
            File(testDirectory).deleteRecursively()
        }
    }

    @Test
    fun responseForward() {
        val (path, output) = createOutputStream("HTTP_RESPONSE_FORWARD")
        val response = HttpResponse(output)
        response.forward("/index.html")
        assertResponse(path)
    }

    @Test
    fun responseRedirect() {
        val (path, output) = createOutputStream("HTTP_RESPONSE_REDIRECT")
        val response = HttpResponse(output)
        response.redirect("/index.html")
        assertResponse(path)
    }

    @Test
    fun responseCookies() {
        val (path, output) = createOutputStream("HTTP_RESPONSE_COOKIE")
        val response = HttpResponse(output)
        response.addHeader("Set-Cookie", "logined=true")
        response.redirect("/index.html")
        assertResponse(path)
    }

    private fun createOutputStream(filename: String): Pair<String, OutputStream> {
        val path = testDirectory + filename
        return Pair(path, FileOutputStream(File(path)))
    }

    private fun assertResponse(fileName: String) {
        val file = File(fileName)
        assertTrue(file.isFile)
    }
}
