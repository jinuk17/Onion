package webserver.framework.controller

import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse


interface Controller {
    fun service(request: HttpRequest, response: HttpResponse)
}