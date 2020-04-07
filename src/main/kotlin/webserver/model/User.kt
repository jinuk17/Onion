package webserver.model

typealias UserId = String

data class User(
    val id: UserId,
    val password: String,
    val name: String,
    val email: String
)

data class Login(
    val userId: UserId,
    val password: String
)