package webserver

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import webserver.framework.http.HttpRequest

class HttpRequestTest {

    @Test
    fun request_GET() {

        val input = javaClass.classLoader.getResourceAsStream("http/HTTP_GET")!!

        val request = HttpRequest(input)

        assertEquals("GET", request.getMethod())
        assertEquals("/user/create", request.getPath())
        assertEquals("keep-alive", request.getHeader("Connection"))
        assertEquals("javajigi", request.getParameter("userId"))

    }

    @Test
    fun request_POST() {

        val input = javaClass.classLoader.getResourceAsStream("http/HTTP_POST")!!
        val request = HttpRequest(input)

        assertEquals("POST", request.getMethod())
        assertEquals("/user/create", request.getPath())
        assertEquals("keep-alive", request.getHeader("Connection"))
        assertEquals("javajigi", request.getParameter("userId"))
    }

    @Test
    fun request_POST2() {

        val input = javaClass.classLoader.getResourceAsStream("http/HTTP_POST2")!!
        val request = HttpRequest(input)

        assertEquals("POST", request.getMethod())
        assertEquals("/user/create", request.getPath())
        assertEquals("keep-alive", request.getHeader("Connection"))
        assertEquals("1", request.getParameter("id"))
        assertEquals("javajigi", request.getParameter("userId"))
    }
}