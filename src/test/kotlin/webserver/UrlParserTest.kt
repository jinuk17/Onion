package webserver

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import webserver.framework.util.UrlParser


class UrlParserTest {

    @Test
    fun test_url_parser() {
        val test = "/user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net"

        val url = UrlParser.parse(test)
        assertEquals("/user/create", url.path)
        assertEquals("javajigi", url.queryParam["userId"])
    }
}

