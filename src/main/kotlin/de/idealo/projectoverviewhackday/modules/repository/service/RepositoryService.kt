package de.idealo.projectoverviewhackday.modules.repository.service

import de.idealo.projectoverviewhackday.clients.repository.RepositoryAdapter
import de.idealo.projectoverviewhackday.clients.repository.RepositoryEntityMapper
import de.idealo.projectoverviewhackday.modules.common.model.ConflictException
import de.idealo.projectoverviewhackday.modules.common.model.NotFoundException
import de.idealo.projectoverviewhackday.modules.repository.service.model.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.GitCommand
import org.eclipse.jgit.api.TransportCommand
import org.eclipse.jgit.transport.CredentialsProvider
import org.springframework.stereotype.Service
import java.io.File
import java.nio.file.Paths

@Service
class RepositoryService(
	private val repositoryServiceProperties: RepositoryServiceProperties,
	private val repositoryAdapter: RepositoryAdapter,
	private val repositoryEntityMapper: RepositoryEntityMapper
) {

	fun getRepositories(): List<Repository> {

		return repositoryAdapter.getRepositories()
			.map { repositoryEntityMapper.map(it) }
	}

	fun createRepository(repository: Repository) {

		if (repositoryAdapter.existsById(repository.name)) {
			throw ConflictException("Repository with name '${repository.name}' already exists.")
		}

		repositoryAdapter.save(repositoryEntityMapper.map(repository))
	}

	fun deleteRepository(name: String) {

		if (!repositoryAdapter.existsById(name)) {
			throw NotFoundException("Repository with name '${name}' not found.")
		}

		repositoryAdapter.deleteById(name)
	}

	fun updateLocalRepositories() {

		getRepositories().forEach { repository ->
			ensureLocalRepositoryIsUpToDate(repository)
		}
	}

	private fun ensureLocalRepositoryIsUpToDate(repository: Repository) {

		val localRepositoryPath = getLocalRepositoryPath(repository)
		if (!localRepositoryPath.exists()) {
			Git.cloneRepository()
				.setURI(repository.cloneUrl)
				.setDirectory(localRepositoryPath)
				.also { configureCredentialsProvider(it, repository) }
				.call()
		} else {
			Git.open(localRepositoryPath)
				.pull()
				.also { configureCredentialsProvider(it, repository) }
				.call()
		}
	}

	private fun <C : GitCommand<T>, T> configureCredentialsProvider(transportCommand: TransportCommand<C, T>, repository: Repository) {
		getCredentialProvider(repository)?.let { credentialsProvider ->
			transportCommand.setCredentialsProvider(credentialsProvider)
		}
	}

	private fun getCredentialProvider(repository: Repository): CredentialsProvider? {
		return null
	}

	private fun getLocalRepositoryPath(repository: Repository): File {
		return Paths.get(repositoryServiceProperties.dir, repository.name).toFile()
	}
}
