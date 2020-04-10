package webserver.framework.util

object UrlParser {

    fun parse(test: String): Url {
        val index = test.indexOf("?")

        if(index == -1) {
            return Url(test)
        }

        val path = test.substring(0, index)
        val params = test.substring(index+1)
        val queryParam = HttpRequestParserUtils.parseQueryParameters(params, "&", "=").orEmpty()
        return Url(path, queryParam)
    }

}

data class Url(val path: String, val queryParam: Map<String, String> = emptyMap())
