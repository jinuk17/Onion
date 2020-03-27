package webserver.util

object UrlParser {

    fun parse(test: String): Url {
        val index = test.indexOf("?")
        val path = test.substring(0, index)
        val params = test.substring(index+1)
        val queryParam = parseQueryParameters(params)
        return Url(path, queryParam)
    }

    private fun parseQueryParameters(text: String?):Map<String, String> {

        if (text.isNullOrBlank()) {
            return mapOf()
        }

        val tokens: List<String> = text.split("&")
        return tokens.mapNotNull { getKeyValue(it, "=") }.toMap()
    }

    private fun getKeyValue(text: String?, separator: String): Pair<String, String>? {

        if(text.isNullOrBlank()) return null

        val tokens = text.split(separator)

        if(tokens.size != 2) return null

        return Pair(tokens[0], tokens[1])
    }
}

data class Url(val path: String, val queryParam: Map<String, String>)
