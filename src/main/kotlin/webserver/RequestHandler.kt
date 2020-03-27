package webserver

import mu.KotlinLogging
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

                        connection.getOutputStream().use { output ->
                            val dos = DataOutputStream(output)
                            val body = Files.readAllBytes(File("./webapp${requestHeader.url}").toPath())
                            response200Header(dos, body.size)
                            responseBody(dos, body)
                        }
                    }
                }
            }
        }catch (e: IOException) {
            logger.error(e) { e }
        }

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

        val method = line1[0]
        val path = line1[1]

        return RequestHeader(method, path)
    }

    data class RequestHeader(val method: String, val url: String)
}
