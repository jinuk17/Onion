package webserver.repository

import webserver.model.User
import webserver.model.UserId

object UserRepository {

    private val map = mutableMapOf(
        "a1" to User("a1", "a1", "기승", "a1@mail.com"),
        "a2" to User("a2", "a2", "미진", "a2@mail.com"),
        "a3" to User("a3", "a3", "범희", "a3@mail.com"),
        "a4" to User("a4", "a4", "현지", "a4@mail.com"),
        "a5" to User("a5", "a5", "진억", "a5@mail.com")
    )

    fun save(user: User): User? {
        return map.put(user.id, user)
    }

    fun get(id: UserId): User? {
        return map[id]
    }

    fun getAll(): List<User> {
        return map.values.toList()
    }
}