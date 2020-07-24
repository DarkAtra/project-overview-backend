package de.idealo.projectoverviewhackday

import de.idealo.projectoverviewhackday.base.CheckConfigurationAdapter
import de.idealo.projectoverviewhackday.base.CheckToRepositoryAdapter
import de.idealo.projectoverviewhackday.base.RepositoryAdapter
import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckToRepository
import de.idealo.projectoverviewhackday.base.model.Repository
import de.idealo.projectoverviewhackday.maven.MavenCheck
import de.idealo.projectoverviewhackday.maven.MavenCheckMode
import de.idealo.projectoverviewhackday.regex.RegexCheck
import de.idealo.projectoverviewhackday.regex.RegexCheckMode
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

		repositoryAdapter.saveAll(listOf(
			Repository(
				name = "health-probes-issue",
				browseUrl = "https://github.com/DarkAtra/health-probes-issue",
				cloneUrl = "https://github.com/DarkAtra/health-probes-issue.git"
			),
			Repository(
				name = "jsr380-kotlin-issue",
				browseUrl = "https://github.com/DarkAtra/jsr380-kotlin-issue",
				cloneUrl = "https://github.com/DarkAtra/jsr380-kotlin-issue.git"
			),
			Repository(
				name = "project-overview-hackday",
				browseUrl = "https://github.com/DarkAtra/project-overview-hackday",
				cloneUrl = "https://github.com/DarkAtra/project-overview-hackday.git"
			)
		))

		checkConfigurationAdapter.saveAll(listOf(
			CheckConfiguration(
				name = "Spring Boot Web Parent Check",
				type = "maven",
				additionalProperties = mapOf(
					MavenCheck.MODE to MavenCheckMode.PARENT.name,
					MavenCheck.GROUP_ID to "org.springframework.boot",
					MavenCheck.ARTIFACT_ID to "spring-boot-starter-parent",
					MavenCheck.VERSION_RESOLVER to "url:https://repo1.maven.org/maven2"
				)
			),
			CheckConfiguration(
				name = "Spring Boot Web Start Check",
				type = "maven",
				additionalProperties = mapOf(
					MavenCheck.MODE to MavenCheckMode.DEPENDENCY.name,
					MavenCheck.GROUP_ID to "org.springframework.boot",
					MavenCheck.ARTIFACT_ID to "spring-boot-starter-web",
					MavenCheck.VERSION_RESOLVER to "url:https://repo1.maven.org/maven2"
				)
			),
			CheckConfiguration(
				name = "Regex Check",
				type = "regex",
				additionalProperties = mapOf(
					RegexCheck.MODE to RegexCheckMode.NORMAL.name,
					RegexCheck.FILE to ".gitignore",
					RegexCheck.PATTERN to "(?s)HELP\\.md.*"
				)
			)
		))

		checkToRepositoryAdapter.saveAll(listOf(
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "health-probes-issue",
					checkId = "Spring Boot Web Parent Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "health-probes-issue",
					checkId = "Spring Boot Web Start Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "health-probes-issue",
					checkId = "Regex Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "jsr380-kotlin-issue",
					checkId = "Spring Boot Web Parent Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "jsr380-kotlin-issue",
					checkId = "Spring Boot Web Start Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "project-overview-hackday",
					checkId = "Spring Boot Web Parent Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "project-overview-hackday",
					checkId = "Spring Boot Web Start Check"
				)
			)
		))
	}
}
