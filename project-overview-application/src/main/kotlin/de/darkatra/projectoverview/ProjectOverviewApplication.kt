package de.darkatra.projectoverview

import de.darkatra.projectoverview.api.LoggingAware
import de.darkatra.projectoverview.context.PluginManager
import de.darkatra.projectoverview.context.PluginProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import java.nio.file.Path
import javax.annotation.PostConstruct

@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties(PluginProperties::class)
class ProjectOverviewApplication(
	private val pluginManager: PluginManager
) : LoggingAware() {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ProjectOverviewApplication>(*args)
		}
	}

	@PostConstruct
	fun post() {
		pluginManager.load(Path.of("C:/Users/DarkAtra/git/project-overview-plugin-maven/target/project-overview-plugin-maven-1.0-SNAPSHOT.jar").normalize().toUri().toURL())
	}
}
