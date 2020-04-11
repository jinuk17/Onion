package webserver.framework.http

import webserver.framework.util.HttpRequestParserUtils
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.IllegalStateException

class HttpRequest(inputStream: InputStream) {

    private val requestLine: RequestLine
    private val header: Map<String, String>
    private val parameter: Map<String, String>

    init {
        val br = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val pathLine = br.readLine() ?: throw IllegalArgumentException()

        requestLine = RequestLine.of(pathLine)

        val headerLines = mutableListOf<String>()
        while (true) {
            val l = br.readLine().takeUnless { it.isNullOrBlank() } ?: break
            headerLines.add(l)
        }

        header = headerLines.mapNotNull {
            HttpRequestParserUtils.getKeyValue(it, ":")
        }.toMap()

        val tempParam = mutableMapOf<String, String>()
        tempParam.putAll(requestLine.params)

        if(requestLine.method == HttpMethod.POST) {
            val bodyParam = header["Content-Length"]?.let { readBody(br, it.toInt()) }?.let {
                HttpRequestParserUtils.parseQueryParameters(it,"&", "=")
            }.orEmpty()
            tempParam.putAll(bodyParam)
        }

        parameter = tempParam.toMap()
    }

    private fun readBody(br: BufferedReader, contentLength: Int): String {
        val body = CharArray(contentLength)
        br.read(body, 0, contentLength)
        return String(body)
    }

    fun getMethod(): HttpMethod {
        return requestLine.method
    }

    fun getPath(): String {
        return requestLine.path
    }

    fun getHeader(name: String): String? {
        return header[name]
    }

    fun getParameter(name: String): String? {
        return parameter[name]
    }

    fun getParameter(): Map<String, String> {
        return parameter
    }

    fun getCookies(): HttpCookie {
        return HttpCookie(getHeader("Cookie").orEmpty())
    }

    fun getSession(): HttpSession {
        val cookie = getCookies().getCookie("JSESSIONID") ?: throw IllegalStateException()
        return HttpSessionRepository.getSession(cookie)
    }

    override fun toString(): String {
        return "HttpRequest(method='${requestLine.method}', path='${requestLine.path}', header=$header, parameter=$parameter)"
    }
}



