package webserver.framework.http


object HttpSessionRepository {

    private val sessionMap: MutableMap<String, HttpSession> = mutableMapOf()

    fun getSession(id: String): HttpSession {
        return sessionMap[id] ?: newSession(id)
    }

    fun remove(id: String) {
        sessionMap.remove(id)
    }

    private fun newSession(id: String): HttpSession {
        val newSession = HttpSession(id)
        sessionMap[id] = newSession
        return newSession
    }

}