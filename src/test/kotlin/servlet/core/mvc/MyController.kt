package servlet.core.mvc

import mu.KotlinLogging
import servlet.core.annotation.Controller
import servlet.core.annotation.RequestMapping
import servlet.core.annotation.RequestMethod
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class MyController {

    private val logger = KotlinLogging.logger {}

    @RequestMapping("/users/findUserId")
    fun findUserId(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        logger.debug { "/users/findUserId" }
        return null
    }

    @RequestMapping(value="/users", method = RequestMethod.POST)
    fun save(request: HttpServletRequest, response: HttpServletResponse): ModelAndView? {
        logger.debug { "save" }
        return null
    }
}