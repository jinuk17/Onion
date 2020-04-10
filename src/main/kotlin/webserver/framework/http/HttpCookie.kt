package webserver.framework.http

import webserver.framework.util.HttpRequestParserUtils


class HttpCookie(cookieString: String) {

    private val cookies: Map<String, String> = HttpRequestParserUtils.parseQueryParameters(cookieString, ";", "=").orEmpty()

    fun getCookie(name: String): String? {
        return cookies[name]
    }

}