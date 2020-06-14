package de.idealo.projectoverviewhackday.service

import de.idealo.projectoverviewhackday.clients.common.RepositoryAdapter
import de.idealo.projectoverviewhackday.model.CheckOutcome
import de.idealo.projectoverviewhackday.model.CheckStatus
import de.idealo.projectoverviewhackday.model.Repository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.nio.file.Path
import java.nio.file.Paths

@Service
class RepositoryService(
	private val repositoryServiceProperties: RepositoryServiceProperties,
	private val repositoryAdapter: RepositoryAdapter
) {

	// TODO: make cron configurable
	// TODO: fetch repositories on application start
	@Scheduled(cron = "0 0/30 * * * ?")
	fun fetchRepositories() {

		repositoryAdapter.getRepositories(repositoryServiceProperties.project)
			.forEach { repository ->
				val localRepositoryPath = getLocalRepositoryPath(repository)
				ensureLocalRepositoryIsUpToDate(repository, localRepositoryPath)
			}
	}

	fun getRepositories(checkOutcome: List<CheckOutcome>, checkStatus: List<CheckStatus>): List<Repository> {

		return repositoryAdapter.getRepositories(repositoryServiceProperties.project)
	}

	private fun ensureLocalRepositoryIsUpToDate(repository: Repository, localRepositoryPath: Path) {

		if (!localRepositoryPath.toFile().exists()) {
			repositoryAdapter.cloneRepository(repository, localRepositoryPath)
		} else {
			repositoryAdapter.pullRepository(localRepositoryPath)
		}
	}

	// TODO: move into Repository class?
	private fun getLocalRepositoryPath(repository: Repository): Path {
		return Paths.get(repositoryServiceProperties.dir, repository.name)
	}
}
