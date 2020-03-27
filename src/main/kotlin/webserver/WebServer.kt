package webserver

import mu.KotlinLogging
import java.net.ServerSocket

object WebServer {

    private val logger = KotlinLogging.logger {}
    private const val DEFAULT_PORT = 8080

    @JvmStatic
    fun main(args: Array<String>) {
        val port = if(args.isEmpty()) DEFAULT_PORT else Integer.parseInt(args[0])

        ServerSocket(port).use {
            logger.info { "Web Application Server started $port port."}

            while(true) {
                val connection = it.accept() ?: break
                val requestHandler = RequestHandler(connection)
                requestHandler.start()
            }
        }
    }
}
