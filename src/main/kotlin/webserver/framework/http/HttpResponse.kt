package webserver.framework.http

import mu.KotlinLogging
import java.io.DataOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream
import java.nio.file.Files

class HttpResponse(output: OutputStream) {

    private val logger = KotlinLogging.logger {}
    private val header: MutableMap<String, String> = mutableMapOf()
    private val dos: DataOutputStream = DataOutputStream(output)

    fun forward(s: String) {
        val body = Files.readAllBytes(File("./webapp$s").toPath())
        header["Content-Type"] = "charset=utf-8"
        write(ResponseStatus.OK, body)
    }

    fun forwardBody(body: ByteArray) {
        header["Content-Type"] = "charset=utf-8"
        write(ResponseStatus.OK, body)
    }

    fun redirect(s: String) {
        header["Location"] = s
        write(ResponseStatus.FOUND)
    }

    fun addHeader(name: String, value: String) {
        header[name] = value
    }

    private fun write(status: ResponseStatus, body: ByteArray? = null) {
        try {
            // Header
            dos.writeBytes("HTTP/1.1 ${status.getHeaderString()} \r\n")
            header.forEach { (name, value) -> dos.writeBytes("$name: $value\r\n") }
            body?.let { dos.writeBytes("Content-Length: ${it.size}\r\n") }
            dos.writeBytes("\r\n")

            // Body
            body?.let { dos.write(it, 0, it.size) }
            dos.flush()
        } catch (e: IOException) {
            logger.error(e) { e }
        }
    }

    enum class ResponseStatus(private val code: Int, private val status: String) {
        OK(200, "OK"),
        FOUND(302, "Found");

        fun getHeaderString(): String {
            return "$code $status"
        }

    }

}