package de.idealo.projectoverviewhackday

import de.idealo.projectoverviewhackday.clients.check.CheckAdapter
import de.idealo.projectoverviewhackday.clients.check.CheckToRepositoryAdapter
import de.idealo.projectoverviewhackday.clients.check.model.CheckEntity
import de.idealo.projectoverviewhackday.clients.check.model.CheckToRepositoryEntity
import de.idealo.projectoverviewhackday.clients.check.model.CheckToRepositoryIdEntity
import de.idealo.projectoverviewhackday.clients.check.model.CheckTypeEntity
import de.idealo.projectoverviewhackday.clients.repository.RepositoryAdapter
import de.idealo.projectoverviewhackday.clients.repository.model.RepositoryEntity
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.scheduling.annotation.EnableScheduling
import javax.annotation.PostConstruct

@EnableCaching
@EnableScheduling
@SpringBootApplication
class ProjectOverviewHackdayApplication(
	private val checkAdapter: CheckAdapter,
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
			RepositoryEntity(
				name = "Java8Features",
				browseUrl = "https://git.darkatra.de/DarkAtra/Java8Features",
				cloneUrl = "https://git.darkatra.de/DarkAtra/Java8Features.git"
			)
		)

		checkAdapter.save(
			CheckEntity(
				name = "Parent Check",
				checkType = CheckTypeEntity.MAVEN,
				additionalProperties = emptyList()
			)
		)
		checkToRepositoryAdapter.save(
			CheckToRepositoryEntity(
				id = CheckToRepositoryIdEntity(
					repositoryId = "Java8Features",
					checkId = "Parent Check"
				)
			)
		)
	}
}
