package webserver.conf

import webserver.framework.controller.AbstractController
import webserver.framework.http.HttpRequest
import webserver.framework.http.HttpResponse

class DefaultController: AbstractController() {

    override fun doGet(request: HttpRequest, response: HttpResponse) {
        val path = request.getPath().takeIf { it != "/" } ?: "/index.html"
        response.forward(path)
    }
}
