package webserver.repository

import webserver.mode.User
import webserver.mode.UserId

object UserRepository {

    private val map = mutableMapOf<UserId, User>()

    fun save(user: User): User? {
        return map.put(user.id, user)
    }

    fun get(id: UserId): User? {
        return map[id]
    }
}