package de.darkatra.projectoverview.base

import de.darkatra.projectoverview.base.model.ConflictException
import de.darkatra.projectoverview.base.model.NotFoundException
import de.darkatra.projectoverview.base.model.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.GitCommand
import org.eclipse.jgit.api.TransportCommand
import org.eclipse.jgit.transport.CredentialsProvider
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class RepositoryService(
	private val repositoryServiceProperties: RepositoryServiceProperties,
	private val repositoryAdapter: RepositoryAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter
) {

	fun getRepositories(): List<Repository> {

		return repositoryAdapter.findAll()
			.onEach { repository ->
				repository.checks = checkToRepositoryAdapter.findByIdRepositoryId(repository.name).map { it.id.checkId }
			}
	}

	fun getRepository(name: String): Repository {

		return repositoryAdapter.findById(name)
			.orElseThrow { NotFoundException("Repository with name '${name}' not found.") }
			.also { repository ->
				repository.checks = checkToRepositoryAdapter.findByIdRepositoryId(repository.name).map { it.id.checkId }
			}
	}

	fun createRepository(repository: Repository): Repository {

		if (repositoryAdapter.existsById(repository.name)) {
			throw ConflictException("Repository with name '${repository.name}' already exists.")
		}

		return repositoryAdapter.save(repository)
	}

	fun deleteRepository(name: String) {

		if (!repositoryAdapter.existsById(name)) {
			throw NotFoundException("Repository with name '${name}' not found.")
		}

		repositoryAdapter.deleteById(name)
	}

	fun upsertAndGetLocalRepository(repository: Repository): Git {

		val localRepositoryPath = getLocalRepositoryPath(repository).toFile()
		return if (!localRepositoryPath.exists()) {
			Git.cloneRepository()
				.setURI(repository.cloneUrl)
				.setDirectory(localRepositoryPath)
				.also { configureCredentialsProvider(it, repository) }
				.call()
		} else {
			Git.open(localRepositoryPath)
				.also { git ->
					git.pull()
						.also { configureCredentialsProvider(it, repository) }
						.call()
				}
		}
	}

	fun getLocalRepositoryPath(repository: Repository): Path {
		return repositoryServiceProperties.dir.resolve(repository.name)
	}

	private fun <C : GitCommand<T>, T> configureCredentialsProvider(transportCommand: TransportCommand<C, T>, repository: Repository) {
		getCredentialProvider(repository)?.let { credentialsProvider ->
			transportCommand.setCredentialsProvider(credentialsProvider)
		}
	}

	private fun getCredentialProvider(repository: Repository): CredentialsProvider? {
		return null
		// TODO: allow configuration of credential providers per repository
		// return UsernamePasswordCredentialsProvider(repositoryServiceProperties.username, repositoryServiceProperties.password)
	}
}
