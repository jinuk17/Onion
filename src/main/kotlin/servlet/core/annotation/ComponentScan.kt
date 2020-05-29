package servlet.core.annotation

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ComponentScan(
    val value: Array<String> = [],
    val basePackages: Array<String> = []
)