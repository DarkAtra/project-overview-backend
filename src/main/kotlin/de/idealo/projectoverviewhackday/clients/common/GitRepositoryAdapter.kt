package de.idealo.projectoverviewhackday.clients.common

import de.idealo.projectoverviewhackday.model.Repository
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.GitCommand
import org.eclipse.jgit.api.TransportCommand
import org.eclipse.jgit.transport.CredentialsProvider
import java.nio.file.Path

abstract class GitRepositoryAdapter : RepositoryAdapter {

	abstract override fun getRepositories(project: String): List<Repository>

	override fun cloneRepository(repository: Repository, localRepositoryPath: Path) {

		Git.cloneRepository()
			.also { configureCredentialsProvider(it) }
			.setURI(repository.cloneUrl)
			.setDirectory(localRepositoryPath.toFile())
			.call()
	}

	override fun pullRepository(localRepositoryPath: Path) {

		Git.open(localRepositoryPath.toFile())
			.pull()
			.also { configureCredentialsProvider(it) }
			.call()
	}

	internal open fun getCredentialProvider(): CredentialsProvider? {
		return null
	}

	private fun <C : GitCommand<T>, T> configureCredentialsProvider(transportCommand: TransportCommand<C, T>) {
		getCredentialProvider()?.let { credentialsProvider ->
			transportCommand.setCredentialsProvider(credentialsProvider)
		}
	}
}
