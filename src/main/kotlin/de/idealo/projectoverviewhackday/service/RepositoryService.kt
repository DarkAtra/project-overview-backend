package de.idealo.projectoverviewhackday.service

import de.idealo.projectoverviewhackday.common.RepositoryAdapter
import de.idealo.projectoverviewhackday.model.Check
import de.idealo.projectoverviewhackday.model.CheckOutcome
import de.idealo.projectoverviewhackday.model.CheckStatus
import de.idealo.projectoverviewhackday.model.Repository
import org.springframework.stereotype.Service

@Service
class RepositoryService(
	private val repositoryServiceProperties: RepositoryServiceProperties,
	private val repositoryAdapter: RepositoryAdapter
) {

	fun getRepositories(checkOutcome: List<CheckOutcome>, checkStatus: List<CheckStatus>): List<Repository> {

		return repositoryAdapter.getRepositories(repositoryServiceProperties.project!!)
			.onEach { performChecks(it, checkOutcome, checkStatus) }
			.filter { it.checkResults.isNotEmpty() }
	}

	private fun performChecks(repository: Repository, checkOutcome: List<CheckOutcome>, checkStatus: List<CheckStatus>) {

		getChecks(repository.name)
			.map { it.check(repository) }
			.filter { checkOutcome.contains(it.checkOutcome) && checkStatus.contains(it.checkStatus) }
			.forEach { repository.checkResults.add(it) }
	}

	private fun getChecks(repository: String): List<Check<out Any>> {

		val excludedChecks = repositoryServiceProperties.checkOverrides!!
			.find { it.repository == repository }
			?.excludedChecks ?: emptyList()

		return repositoryServiceProperties.checks!!
			.mapNotNull { it.build(excludedChecks) }
	}
}
