package webserver

import mu.KotlinLogging
import webserver.model.Login
import webserver.model.User
import webserver.repository.UserRepository
import webserver.util.HttpRequestParserUtil
import java.io.*
import java.net.Socket
import java.nio.file.Files

class RequestHandler(private val connection: Socket) : Thread() {

    private val logger = KotlinLogging.logger {}

    override fun run() {

        logger.info { "New Client Connect! Connected IP : ${this.connection.inetAddress}, Port : ${connection.port}" }

        try{
            connection.getInputStream().use { input ->

                val httpRequest = HttpRequest(input)

                logger.info {  httpRequest }

                connection.getOutputStream().use { output ->

                    val httpResponse = HttpResponse(output)
                    val method = httpRequest.getMethod()
                    val path = httpRequest.getPath()

                    when(method){
                        "GET" -> {
                            if(path.toLowerCase().endsWith(".html") || path.toLowerCase().endsWith(".css")) {
                                httpResponse.forward(path)
                            }else if(path.toLowerCase().startsWith("/user/list")) {
                                if(checkAuthorized(httpRequest.getHeader("Cookie"))) {
                                    val users =
                                        UserRepository.getAll().joinToString("<br/>") { "<h3>$it</h3>" }
                                    httpResponse.forwardBody(users.toByteArray())
                                }else{
                                    httpResponse.redirect("/user/login.html")
                                }
                            }
                        }
                        "POST" -> {
                            if(path.toLowerCase().startsWith("/user/create")) {
                                val user = handleUserCreate(httpRequest)
                                if(user != null) {
                                    httpResponse.redirect("/index.html")
                                }
                            }else if(path.toLowerCase().startsWith("/user/login")) {
                                val login = parseLogin(httpRequest)
                                val loginUser = login?.let { l ->
                                    UserRepository.get(l.userId)?.takeIf { it.password == l.password }
                                }

                                if(loginUser != null) {
                                    httpResponse.addHeader("Set-Cookie", "logined=true")
                                    httpResponse.redirect("/index.html")
                                }else{
                                    httpResponse.addHeader("Set-Cookie", "logined=false")
                                    httpResponse.redirect("/user/login_failed.html")
                                }
                            }
                        }
                    }
                }
            }
        }catch (e: IOException) {
            logger.error(e) { e }
        }

    }

    private fun checkAuthorized(cookie: String?): Boolean {
        return cookie?.let {
            HttpRequestParserUtil.parseQueryParameters(it, ";", "=")
        }?.get("logined")?.toBoolean() ?: false
    }

    private fun handleUserCreate(request: HttpRequest): User? {

        val user =
            request.getParameter("userId")?.let { userId ->
                request.getParameter("password")?.let { password ->
                    request.getParameter("name")?.let { name ->
                        request.getParameter("email")?.let { email ->
                            User(userId, password, name, email)
                        }
                    }
                }
            }

        logger.info { user }

        return user?.let { UserRepository.save(it) }
    }

    private fun parseLogin(request: HttpRequest): Login? {
        return request.getParameter("userId")?.let { userId ->
            request.getParameter("password")?.let { password ->
                Login(userId, password)
            }
        }
    }
}
