package servlet.app.service

import servlet.app.repository.QuestionRepository
import servlet.app.repository.UserRepository
import servlet.core.annotation.Inject
import servlet.core.annotation.Service

@Service
class MyQnaService @Inject constructor(
    private val userRepository: UserRepository,
    private val questionRepository: QuestionRepository
) {


}
