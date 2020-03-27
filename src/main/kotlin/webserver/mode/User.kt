package webserver.mode

typealias UserId = String

data class User(


    val id: UserId,
    val password: String,
    val name: String,
    val email: String
)