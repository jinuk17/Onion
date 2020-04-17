package servlet.core

import mu.KotlinLogging
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator
import servlet.core.db.ConnectionManager
import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class ContextLoaderListener: ServletContextListener {

    private val logger = KotlinLogging.logger {}

    override fun contextInitialized(sce: ServletContextEvent?) {
        val populator = ResourceDatabasePopulator()
        populator.addScript(ClassPathResource("jwp.sql"))
        DatabasePopulatorUtils.execute(populator, ConnectionManager.getDataSource())

        logger.info("Completed load ServletContext!")
    }

    override fun contextDestroyed(sce: ServletContextEvent?) {}
}