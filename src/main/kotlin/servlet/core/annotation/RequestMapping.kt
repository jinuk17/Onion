package servlet.core.annotation

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RequestMapping(val value: String = "", val method: RequestMethod = RequestMethod.GET)