package webserver

import mu.KotlinLogging
import webserver.util.HttpRequestParserUtil
import webserver.util.UrlParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

class HttpRequest(inputStream: InputStream) {

    private val logger = KotlinLogging.logger {}

    private val method: String
    private val path: String
    private val header: Map<String, String>
    private val parameter: Map<String, String>

    init {
        val br = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val pathLine = br.readLine() ?: throw IllegalArgumentException()
        val (m, p) = processRequestLine(pathLine)
        val url = UrlParser.parse(p)

        val headerLines = mutableListOf<String>()
        while (true) {
            val l = br.readLine().takeUnless { it.isNullOrBlank() } ?: break
            headerLines.add(l)
        }

        header = headerLines.mapNotNull {
            HttpRequestParserUtil.getKeyValue(it, ":")
        }.toMap()

        val tempParam = mutableMapOf<String, String>()
        tempParam.putAll(url.queryParam)

        if(m == "POST") {
           val bodyParam = header["Content-Length"]?.let { readBody(br, it.toInt()) }?.let {
               HttpRequestParserUtil.parseQueryParameters(it,"&", "=")
           }.orEmpty()
           tempParam.putAll(bodyParam)
        }

        method = m
        path = url.path
        parameter = tempParam.toMap()
    }

    private fun processRequestLine(line: String): Pair<String, String> {
        val requestLine = line.split(" ")
        return Pair(requestLine[0], requestLine[1])
    }

    private fun readBody(br: BufferedReader, contentLength: Int): String {
        val body = CharArray(contentLength)
        br.read(body, 0, contentLength)
        return String(body)
    }

    fun getMethod(): String {
        return method
    }

    fun getPath(): String {
        return path
    }

    fun getHeader(name: String): String? {
        return header[name]
    }

    fun getParameter(name: String): String? {
        return parameter[name]
    }

    override fun toString(): String {
        return "HttpRequest(method='$method', path='$path', header=$header, parameter=$parameter)"
    }

}



