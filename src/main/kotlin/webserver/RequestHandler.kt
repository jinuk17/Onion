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

                    val dos = DataOutputStream(output)

                    var body: ByteArray? = null
                    var redirectLocation: String? = null

                    val method = httpRequest.getMethod()
                    val path = httpRequest.getPath()

                    when(method){
                        "GET" -> {
                            if(path.toLowerCase().endsWith(".html") || path.toLowerCase().endsWith(".css")) {
                                body = handleIndexHtml(path)
                            }else if(path.toLowerCase().startsWith("/user/create")) {
                                val user = handleUserCreate(httpRequest)
                                body = user?.toString()?.toByteArray()
                            }else if(path.toLowerCase().startsWith("/user/list")) {
                                if(checkAuthorized(httpRequest.getHeader("Cookie"))) {
                                    val users =
                                        UserRepository.getAll().joinToString("<br/>") { "<h3>$it</h3>" }
                                    logger.info {users}
                                    body = users.toByteArray()
                                }else{
                                    redirectLocation = "/user/login.html"
                                }
                            }

                            if(body != null) {
                                response200Header(dos, body.size, parseAcceptType(httpRequest.getHeader("Accept")))
                                responseBody(dos, body)
                            }else if(redirectLocation != null) {
                                response302Header(dos, redirectLocation)
                            }
                        }
                        "POST" -> {
                            if(path.toLowerCase().startsWith("/user/create")) {
                                val user = handleUserCreate(httpRequest)
                                if(user != null) {
                                    response302Header(dos, "/index.html")
                                }
                            }else if(path.toLowerCase().startsWith("/user/login")) {
                                val login = parseLogin(httpRequest)
                                val loginUser = login?.let { l ->
                                    UserRepository.get(l.userId)?.takeIf { it.password == l.password }
                                }

                                if(loginUser != null) {
                                    response302Header(dos, "/index.html", "logined=true")
                                }else{
                                    response302Header(dos, "/user/login_failed.html", "logined=false")
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

    private fun parseAcceptType(acceptType: String?): String {
        val accepts = acceptType?.split(",").orEmpty()
        return if(accepts.contains("text/css")) "text/css" else "text/html"
    }


    private fun responseBody(dos: DataOutputStream, body: ByteArray) {
        try {
            dos.write(body, 0, body.size)
            dos.flush()
        } catch (e: IOException) {
            logger.error(e) { e }
        }
    }

    private fun response200Header(dos: DataOutputStream, lengthOfBodyContent: Int, contentType: String) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n")
            dos.writeBytes("Content-Type: $contentType;charset=utf-8\r\n")
            dos.writeBytes("Content-Length: $lengthOfBodyContent\r\n")
            dos.writeBytes("\r\n")
        } catch (e: IOException) {
            logger.error(e) { e }
        }
    }

    private fun response302Header(dos: DataOutputStream, location: String, cookie: String? = null) {
        try {
            dos.writeBytes("HTTP/1.1 302 OK \r\n")
            dos.writeBytes("Location: $location\r\n")
            cookie?.let{ dos.writeBytes("Set-Cookie: $it\r\n") }
            dos.writeBytes("\r\n")
        } catch (e: IOException) {
            logger.error(e) { e }
        }
    }

    /*
    * GET /index.html HTTP/1.1
    * Host: localhost:8080
    * Connection: Keep-Alive
    * User-Agent: Apache-HttpClient/4.5.10 (Java/11.0.5)
    * Accept-Encoding: gzip,deflate
    * */
    private fun requestHeader(br: BufferedReader) : RequestHeader? {

        val header = arrayListOf<String>()

        while (true) {
            val line = br.readLine()

            if (line == null || line.isEmpty()) break

            header.add(line)
        }

        if(header.isEmpty()) return null

        val line1 = header[0].split(" ")

        val method = HttpRequestMethod.valueOf(line1[0])
        val path = line1[1]


        val metadata = header.subList(1, header.size).mapNotNull {
            HttpRequestParserUtil.getKeyValue(it, ":")
        }.toMap()

        return RequestHeader(method, path, metadata)
    }

    data class RequestHeader(val method: HttpRequestMethod, val url: String, val metadata: Map<String, String>)

    enum class HttpRequestMethod {
        GET, POST
    }

    private fun handleIndexHtml(path: String): ByteArray {
        return Files.readAllBytes(File("./webapp$path").toPath())
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

    private fun readBody(br: BufferedReader, contentLength: Int): String {
        val body = CharArray(contentLength)
        br.read(body, 0, contentLength)
        return String(body)
    }
}
