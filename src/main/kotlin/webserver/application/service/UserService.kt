package webserver.application.service

import servlet.app.model.Login
import servlet.app.model.User
import webserver.application.repository.OUserRepository


class UserService {

    fun login(login: Login): User? {
        return OUserRepository.get(login.userId)?.takeIf { it.password == login.password }
    }

}