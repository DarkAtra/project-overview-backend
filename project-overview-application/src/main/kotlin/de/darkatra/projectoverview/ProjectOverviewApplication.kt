package de.darkatra.projectoverview

import de.darkatra.projectoverview.context.ArgumentResolverRegistry
import de.darkatra.projectoverview.context.CheckContext
import de.darkatra.projectoverview.context.InvokablePluginTarget
import de.darkatra.projectoverview.context.PluginManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.util.LinkedCaseInsensitiveMap
import java.nio.file.Path
import javax.annotation.PostConstruct

@SpringBootApplication(proxyBeanMethods = false)
class ProjectOverviewApplication(
	private val pluginManager: PluginManager,
	private val argumentResolverRegistry: ArgumentResolverRegistry
) {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ProjectOverviewApplication>(*args)
		}
	}

	@PostConstruct
	fun post() {

		val plugin = pluginManager.load(Path.of("C:/Users/DarkAtra/git/project-overview-plugin-maven/target/project-overview-plugin-maven-1.0-SNAPSHOT.jar").toUri().normalize().toURL())

		val checkContext = CheckContext(
			checkName = "user supplied name for the check",
			parameters = LinkedCaseInsensitiveMap()
		)

		InvokablePluginTarget(plugin, argumentResolverRegistry).performCheck("maven", checkContext)
	}
}
