package de.darkatra.projectoverview

import de.darkatra.projectoverview.context.PluginManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.nio.file.Path
import javax.annotation.PostConstruct

@SpringBootApplication(proxyBeanMethods = false)
class ProjectOverviewApplication(
	private val pluginManager: PluginManager
) {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ProjectOverviewApplication>(*args)
		}
	}

	@PostConstruct
	fun post() {

		pluginManager.load(Path.of("./plugin.jar").toUri().normalize().toURL())
	}
}
