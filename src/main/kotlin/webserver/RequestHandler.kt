package webserver

import mu.KotlinLogging
import java.io.DataOutputStream
import java.io.IOException
import java.lang.Exception
import java.net.Socket

class RequestHandler(private val connection: Socket) : Thread() {

    private val logger = KotlinLogging.logger {}

    override fun run() {

        logger.debug { "New Client Connect! Connected IP : ${this.connection.inetAddress}, Port : ${connection.port}" }

        try{
            connection.getInputStream().use { input ->
                connection.getOutputStream().use { output ->
                    val dos = DataOutputStream(output)
                    val body = "Hello World".toByteArray()
                    response200Header(dos, body.size)
                    responseBody(dos, body)
                }
            }
        }catch (e: IOException) {
            logger.error{ e }
        }

    }

    private fun responseBody(dos: DataOutputStream, body: ByteArray) {
        try {
            dos.write(body, 0, body.size)
            dos.flush()
        } catch (e: IOException) {
            logger.error(e){e.message}
        }
    }

    private fun response200Header(dos: DataOutputStream, lengthOfBodyContent: Int) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n")
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n")
            dos.writeBytes("Content-Length: $lengthOfBodyContent\r\n")
            dos.writeBytes("\r\n")
        } catch (e: IOException) {
            logger.error { e }
        }
    }
}
