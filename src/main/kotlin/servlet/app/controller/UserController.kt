package servlet.app.controller

import servlet.app.dao.UserDao
import servlet.app.model.User
import servlet.app.model.UserNotFoundException
import servlet.core.annotation.Controller
import servlet.core.annotation.Inject
import servlet.core.annotation.RequestMapping
import servlet.core.annotation.RequestMethod
import servlet.core.mvc.LegacyController.Companion.jspView
import servlet.core.mvc.LegacyController.Companion.redirectView
import servlet.core.mvc.ModelAndView
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class UserController @Inject constructor(private val userDao: UserDao) {

    @RequestMapping("/users")
    fun list(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        if (!Authentication.hasUserSession(request)) {
            return redirectView("/users/loginForm")
        }

        val mav = jspView("/user/list.jsp")
        mav.addObject("users", userDao.findAll())
        return mav
    }

    @RequestMapping("/user/profile")
    fun profile(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        val userId = request.getParameter("userId")
        val user = userDao.findById(userId) ?: throw UserNotFoundException()

        return jspView("/user/profile.jsp").addObject("user", user)
    }

    @RequestMapping("/users/form")
    fun form(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView("/user/form.jsp")
    }


    @RequestMapping("/user/create", method = RequestMethod.POST)
    fun create(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        val user = User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email")
        )

        userDao.insert(user)
        val session = request.session
        session.setAttribute("user", user)
        return redirectView("/")
    }

    @RequestMapping("/user/updateForm")
    fun updateForm(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {
        return jspView("/user/updateForm.jsp")
    }


    @RequestMapping("/user/update")
    fun update(request: HttpServletRequest, response: HttpServletResponse): ModelAndView {

        val user = userDao.findById(request.getParameter("userId"))
        val sessionUser = Authentication.getSessionUser(request) ?: return redirectView("/users/loginForm")
        if(sessionUser.id != user?.id) throw IllegalArgumentException("다른 사용자의 정보를 수정할 수 없습니다.")

        val updateUser = User(
            request.getParameter("userId"),
            request.getParameter("password"),
            request.getParameter("name"),
            request.getParameter("email")
        )

        userDao.update(updateUser)
        return redirectView("/users")
    }
}