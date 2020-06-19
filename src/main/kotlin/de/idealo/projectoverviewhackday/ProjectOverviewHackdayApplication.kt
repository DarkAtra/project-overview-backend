package de.idealo.projectoverviewhackday

import de.idealo.projectoverviewhackday.base.CheckConfigurationAdapter
import de.idealo.projectoverviewhackday.base.CheckToRepositoryAdapter
import de.idealo.projectoverviewhackday.base.RepositoryAdapter
import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckToRepository
import de.idealo.projectoverviewhackday.base.model.CheckType
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
				name = "Java8Features",
				browseUrl = "https://git.darkatra.de/DarkAtra/Java8Features",
				cloneUrl = "https://git.darkatra.de/DarkAtra/Java8Features.git"
			)
		)

		checkConfigurationAdapter.save(
			CheckConfiguration(
				name = "Parent Check",
				checkType = CheckType.MAVEN,
				additionalProperties = mapOf(
					MavenCheck.MODE to "dependency",
					MavenCheck.GROUP_ID to "junit",
					MavenCheck.ARTIFACT_ID to "junit",
					MavenCheck.VERSION to "4.12"
				)
			)
		)
		checkToRepositoryAdapter.save(
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "Java8Features",
					checkId = "Parent Check"
				)
			)
		)
	}
}
