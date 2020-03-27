package webserver

import mu.KotlinLogging
import webserver.mode.Login
import webserver.mode.User
import webserver.repository.UserRepository
import webserver.util.HttpRequestParserUtil
import webserver.util.UrlParser
import java.io.*
import java.net.Socket
import java.nio.file.Files

class RequestHandler(private val connection: Socket) : Thread() {

    private val logger = KotlinLogging.logger {}

    override fun run() {

        logger.info { "New Client Connect! Connected IP : ${this.connection.inetAddress}, Port : ${connection.port}" }

        try{
            connection.getInputStream().use { input ->
                InputStreamReader(input, "UTF-8").use { isr ->
                    BufferedReader(isr).use { br ->

                        val requestHeader = requestHeader(br) ?: return

                        val requestBody = requestHeader.metadata["Content-Length"]?.let {
                            readBody(br, it.toInt())
                        }.orEmpty()

                        logger.info { "============================================================ "}
                        logger.info { "head :  : $requestHeader "}
                        logger.info { "body :  : $requestBody "}
                        logger.info { "============================================================ "}

                        connection.getOutputStream().use { output ->

                            val dos = DataOutputStream(output)

                            requestHeader.apply {

                                var body: ByteArray? = null
                                var redirectLocation: String? = null

                                when(method){
                                    HttpRequestMethod.GET -> {
                                        if(url.toLowerCase().endsWith(".html")) {
                                            body = handleIndexHtml(requestHeader)
                                        }else if(url.toLowerCase().startsWith("/user/create")) {
                                            val user = handleUserCreate(UrlParser.parse(url).queryParam)
                                            body = user?.toString()?.toByteArray()
                                        }else if(url.toLowerCase().startsWith("/user/list")) {
                                            if(checkAuthorized(requestHeader)) {
                                                val users =
                                                    UserRepository.getAll().joinToString("<br/>") { "<h3>$it</h3>" }
                                                logger.info {users}
                                                body = users.toByteArray()
                                            }else{
                                                redirectLocation = "/user/login.html"
                                            }
                                        }

                                        if(body != null) {
                                            response200Header(dos, body.size)
                                            responseBody(dos, body)
                                        }else if(redirectLocation != null) {
                                            response302Header(dos, redirectLocation)
                                        }
                                    }
                                    HttpRequestMethod.POST -> {

                                        val parameterMap = requestBody.let {
                                            HttpRequestParserUtil.parseQueryParameters(it,"&", "=")
                                        }.orEmpty()

                                        if(url.toLowerCase().startsWith("/user/create")) {
                                            val user = handleUserCreate(parameterMap)
                                            if(user != null) {
                                                response302Header(dos, "/index.html")
                                            }
                                        }else if(url.toLowerCase().startsWith("/user/login")) {
                                            val login = parseLogin(parameterMap)
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

                    }
                }
            }
        }catch (e: IOException) {
            logger.error(e) { e }
        }

    }

    private fun checkAuthorized(requestHeader: RequestHeader): Boolean {
        return requestHeader.metadata["Cookie"]?.let {
            HttpRequestParserUtil.parseQueryParameters(it, ";", "=")
        }?.get("logined")?.toBoolean() ?: false
    }

    private fun responseBody(dos: DataOutputStream, body: ByteArray) {
        try {
            dos.write(body, 0, body.size)
            dos.flush()
        } catch (e: IOException) {
            logger.error(e) { e }
        }
    }

    private fun response200Header(dos: DataOutputStream, lengthOfBodyContent: Int) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n")
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n")
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

    private fun handleIndexHtml(requestHeader: RequestHeader): ByteArray {
        return Files.readAllBytes(File("./webapp${requestHeader.url}").toPath())
    }

    private fun handleUserCreate(queryParam: Map<String, String>): User? {

        val user =
            queryParam["userId"]?.let { userId ->
            queryParam["password"]?.let { password ->
                queryParam["name"]?.let { name ->
                    queryParam["email"]?.let { email ->
                        User(userId, password, name, email)
                    }
                }
            }
        }

        logger.info { user }

        return user?.let { UserRepository.save(it) }
    }

    private fun parseLogin(queryParam: Map<String, String>): Login? {
        return queryParam["userId"]?.let { userId ->
            queryParam["password"]?.let { password ->
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
