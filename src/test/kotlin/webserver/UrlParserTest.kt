package webserver

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import webserver.util.UrlParser


class UrlParserTest {

    @Test
    fun test_url_parser() {
        val test = "/user/create?userId=javajigi&password=password&name=JaeSung&email=javajigi%40slipp.net"

        val url = UrlParser.parse(test)
        Assertions.assertEquals("/user/create", url.path)
        Assertions.assertEquals("javajigi", url.queryParam["userId"])
    }
}

