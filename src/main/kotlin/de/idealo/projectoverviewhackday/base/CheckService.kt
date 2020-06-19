package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckResult
import org.springframework.stereotype.Service

@Service
class CheckService(
	private val checkBuilder: CheckBuilder,
	private val checkConfigurationAdapter: CheckConfigurationAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter,
	private val repositoryService: RepositoryService
) {

	fun getCheckConfigurations(): List<CheckConfiguration> {

		return checkConfigurationAdapter.findAll()
	}

	fun performChecks(repositoryName: String): List<CheckResult> {

		val checks = checkToRepositoryAdapter.findByIdRepositoryId(repositoryName)
			.mapNotNull { checkConfigurationAdapter.findById(it.id.checkId).orElse(null) }

		val repository = repositoryService.getRepository(repositoryName)
		return repositoryService.upsertAndGetLocalRepository(repository).use { git ->

			val localRepositoryPath = repositoryService.getLocalRepositoryPath(repository)
			checks.map { checkConfiguration ->
				val check = checkBuilder.getCheck(
					checkConfiguration = checkConfiguration
				)

				check.performCheck(localRepositoryPath, checkConfiguration.additionalProperties)
			}
		}
	}
}
