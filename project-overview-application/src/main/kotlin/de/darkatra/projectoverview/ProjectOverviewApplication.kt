package de.darkatra.projectoverview

import de.darkatra.projectoverview.context.DefaultCheckContext
import de.darkatra.projectoverview.context.PluginManager
import de.darkatra.projectoverview.context.PluginProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.util.LinkedCaseInsensitiveMap
import java.nio.file.Path
import javax.annotation.PostConstruct

@SpringBootApplication(proxyBeanMethods = false)
@EnableConfigurationProperties(PluginProperties::class)
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

		val plugin = pluginManager.load(Path.of("C:/Users/DarkAtra/git/project-overview-plugin-maven/target/project-overview-plugin-maven-1.0-SNAPSHOT.jar").normalize().toUri().toURL())

		val checkContext = DefaultCheckContext(
			checkName = "Spring Boot Parent Check",
			repositoryDirectory = Path.of("./repositories/project-overview-backend").normalize(),
			parameters = LinkedCaseInsensitiveMap<String>().also {
				it["groupId"] = "org.springframework.boot"
				it["artifactId"] = "spring-boot-starter-web"
				it["versionResolver"] = "url:https://repo1.maven.org/maven2"
			}
		)

		pluginManager.createInvokablePluginTarget(plugin).performCheck("parent", checkContext)
	}
}
