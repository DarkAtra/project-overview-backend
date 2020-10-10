package de.darkatra.projectoverview

import de.darkatra.projectoverview.base.CheckConfigurationAdapter
import de.darkatra.projectoverview.base.CheckToRepositoryAdapter
import de.darkatra.projectoverview.base.RepositoryAdapter
import de.darkatra.projectoverview.base.model.CheckConfiguration
import de.darkatra.projectoverview.base.model.CheckToRepository
import de.darkatra.projectoverview.base.model.Repository
import de.darkatra.projectoverview.maven.MavenCheck
import de.darkatra.projectoverview.maven.MavenCheckMode
import de.darkatra.projectoverview.regex.RegexCheck
import de.darkatra.projectoverview.regex.RegexCheckMode
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@EnableCaching
@EnableScheduling
@SpringBootApplication(proxyBeanMethods = false)
class ProjectOverviewApplication(
	private val checkConfigurationAdapter: CheckConfigurationAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter,
	private val repositoryAdapter: RepositoryAdapter
) {
	companion object {
		@JvmStatic
		fun main(args: Array<String>) {
			runApplication<ProjectOverviewApplication>(*args)
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
				name = "project-overview-backend",
				browseUrl = "https://github.com/DarkAtra/project-overview-backend",
				cloneUrl = "https://github.com/DarkAtra/project-overview-backend.git"
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
					repositoryId = "project-overview-backend",
					checkId = "Spring Boot Web Parent Check"
				)
			),
			CheckToRepository(
				id = CheckToRepository.CheckToRepositoryId(
					repositoryId = "project-overview-backend",
					checkId = "Spring Boot Web Start Check"
				)
			)
		))
	}
}
