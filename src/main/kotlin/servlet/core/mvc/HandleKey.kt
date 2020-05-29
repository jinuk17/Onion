package servlet.core.mvc

import servlet.core.annotation.RequestMethod

data class HandleKey(
    private val url: String,
    private val requestMethod: RequestMethod
)
