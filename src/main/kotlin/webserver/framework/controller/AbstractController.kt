package webserver.framework.controller

import webserver.framework.http.HttpMethod
import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse
import java.lang.UnsupportedOperationException


abstract class AbstractController : Controller {

    override fun service(request: HttpRequest, response: HttpResponse) {
        when(request.getMethod()) {
            HttpMethod.POST -> doPost(request, response)
            HttpMethod.GET -> doGet(request, response)
        }
    }

    open fun doGet(request: HttpRequest, response: HttpResponse) {
        throw UnsupportedOperationException()
    }
    open fun doPost(request: HttpRequest, response: HttpResponse) {
        throw UnsupportedOperationException()
    }
}