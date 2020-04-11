package webserver.framework.http

class HttpSession(private val id: String) {

    private val attributes: MutableMap<String, Any> = mutableMapOf()

    fun getId(): String {
        return id
    }

    fun getAttribute(name: String): Any? {
        return attributes[name]
    }

    fun setAttribute(name: String, value: Any) {
        attributes[name] = value
    }

    fun removeAttribute(name: String) {
        attributes.remove(name)
    }

    fun invalidate() {
        HttpSessionRepository.remove(id)
    }
}