package de.darkatra.projectoverview.git

import org.eclipse.jgit.api.Git
import org.springframework.stereotype.Service

@Service
class RepositoryService(
	private val repositoryServiceProperties: RepositoryServiceProperties
) {

	// TODO: persist repositories
	fun createRepository() {

	}

	// TODO: read from persistent storage
	fun getRepository(repositorySlug: String): Repository {

		return Repository(
			name = "project-overview-backend",
			browseUrl = "https://github.com/DarkAtra/project-overview-backend",
			cloneUrl = "https://github.com/DarkAtra/project-overview-backend.git"
		)
	}

	fun upsertAndGetLocalRepository(repository: Repository): Git {

		val localRepositoryPath = repositoryServiceProperties.dir.resolve(repository.slug).toFile()
		return if (!localRepositoryPath.exists()) {
			Git.cloneRepository()
				.setURI(repository.cloneUrl)
				.setDirectory(localRepositoryPath)
				.call()
		} else {
			Git.open(localRepositoryPath)
				.also { git ->
					git.pull().call()
				}
		}
	}
}
