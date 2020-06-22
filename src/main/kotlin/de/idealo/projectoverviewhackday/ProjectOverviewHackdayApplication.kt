package de.idealo.projectoverviewhackday

import de.idealo.projectoverviewhackday.base.CheckConfigurationAdapter
import de.idealo.projectoverviewhackday.base.CheckToRepositoryAdapter
import de.idealo.projectoverviewhackday.base.RepositoryAdapter
import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckToRepository
import de.idealo.projectoverviewhackday.base.model.Repository
import de.idealo.projectoverviewhackday.maven.MavenCheck
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@EnableCaching
@EnableScheduling
@SpringBootApplication
class ProjectOverviewHackdayApplication(
	private val checkConfigurationAdapter: CheckConfigurationAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter,
	private val repositoryAdapter: RepositoryAdapter
) {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ProjectOverviewHackdayApplication>(*args)
		}
	}

	@PostConstruct
	fun post() {

		repositoryAdapter.save(
			Repository(
				name = "health-probes-issue",
				browseUrl = "https://github.com/DarkAtra/health-probes-issue",
				cloneUrl = "https://github.com/DarkAtra/health-probes-issue.git"
			)
		)

		checkConfigurationAdapter.save(
			CheckConfiguration(
				name = "Spring Boot Web Start Check",
				type = "maven",
				additionalProperties = mapOf(
					MavenCheck.MODE to "dependency",
					MavenCheck.GROUP_ID to "org.springframework.boot",
					MavenCheck.ARTIFACT_ID to "spring-boot-starter-web",
					MavenCheck.VERSION_RESOLVER to "url:https://repo1.maven.org/maven2"
				)
			)
		)
		checkToRepositoryAdapter.save(
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "health-probes-issue",
					checkId = "Spring Boot Web Start Check"
				)
			)
		)
	}
}
