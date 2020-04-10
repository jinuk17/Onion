package servlet

import mu.KotlinLogging
import org.apache.catalina.startup.Tomcat
import org.apache.catalina.webresources.DirResourceSet
import org.apache.catalina.webresources.StandardRoot
import java.io.File

/*
* output path 추가
* https://www.slipp.net/questions/302
* */
object WebServeLauncher {

    private val logger = KotlinLogging.logger {}

    @JvmStatic
    fun main(args: Array<String>) {
        val webappDirection = "webapp/"
        val tomcat = Tomcat()

        tomcat.setPort(8080)

        val context = tomcat.addWebapp("/", File(webappDirection).absolutePath)
        val additionWebInfClasses = File("out/production")
        val standardRoot = StandardRoot(context)
        standardRoot.addPreResources(DirResourceSet(standardRoot, "/WEB-INF/classes", additionWebInfClasses.absolutePath, "/"))
        context.resources = standardRoot

        logger.info("configuring app with basedir: ${File("/$webappDirection").absolutePath}")

        tomcat.start()
        tomcat.server.await()
    }
}