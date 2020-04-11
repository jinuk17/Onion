package webserver.application.service

import webserver.application.model.Login
import webserver.application.model.User
import webserver.application.repository.UserRepository


class UserService {

    fun login(login: Login): User? {
        return UserRepository.get(login.userId)?.takeIf { it.password == login.password }
    }

}