package webserver.framework

import mu.KotlinLogging
import webserver.conf.DefaultController
import webserver.conf.RequestMapping
import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse
import webserver.framework.util.HttpRequestParserUtils
import java.io.IOException
import java.net.Socket
import java.util.*

class RequestHandler(private val connection: Socket) : Thread() {

    private val logger = KotlinLogging.logger {}
    private val defaultController = DefaultController()

    override fun run() {

        logger.info { "New Client Connect! Connected IP : ${this.connection.inetAddress}, Port : ${connection.port}" }

        try{
            connection.getInputStream().use { input ->
                val request = HttpRequest(input)
                logger.info {  request }
                connection.getOutputStream().use { output ->
                    val controller = RequestMapping.getController(request.getPath()) ?: defaultController
                    val response = HttpResponse(output)

                    if(request.getCookies().getCookie("JSESSIONID") == null) {
                        response.addHeader("Set-Cookie", "JSESSIONID=${UUID.randomUUID()}")
                    }

                    controller.service(request, HttpResponse(output))

                }
            }
        }catch (e: IOException) {
            logger.error(e) { e }
        }
    }
}
