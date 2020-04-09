package webserver.framework.http

import mu.KotlinLogging
import webserver.framework.util.UrlParser
import java.lang.IllegalArgumentException

data class RequestLine(
    val method: HttpMethod,
    val path: String,
    val params: Map<String, String>
) {
    // TODO : TEST 코드 추가
    companion object {
        private val logger = KotlinLogging.logger {}
        fun of(requestLine: String): RequestLine {
            logger.debug { "request line : $requestLine" }
            val tokens = requestLine.split(" ")

            if (tokens.size != 3) throw IllegalArgumentException("Invalid request line : $requestLine")
            val url = UrlParser.parse(tokens[1])

            return RequestLine(
                HttpMethod.valueOf(tokens[0]),
                url.path,
                url.queryParam
            )
        }
    }
}