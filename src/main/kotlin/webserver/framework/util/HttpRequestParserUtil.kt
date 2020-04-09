package webserver.framework.util

object HttpRequestParserUtil {

    fun parseQueryParameters(text: String?, parameterDelimiter: String, keyValueDelimiter: String):Map<String, String>? {

        if (text.isNullOrBlank()) {
            return mapOf()
        }

        val tokens: List<String> = text.split(parameterDelimiter)
        return tokens.mapNotNull {
            getKeyValue(
                it,
                keyValueDelimiter
            )
        }.toMap()
    }

    fun getKeyValue(text: String?, separator: String): Pair<String, String>? {

        if(text.isNullOrBlank()) return null

        val tokens = text.split(separator)

        if(tokens.size != 2) return null

        return Pair(tokens[0].trim(), tokens[1].trim())
    }
}