package de.idealo.projectoverviewhackday.clients.bitbucket

import de.idealo.projectoverviewhackday.clients.common.GitRepositoryAdapter
import de.idealo.projectoverviewhackday.model.Repository
import org.eclipse.jgit.transport.CredentialsProvider
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider

open class BitBucketRepositoryAdapter(
	private val bitBucketClient: BitBucketClient,
	private val bitBucketAdapterProperties: BitBucketAdapterProperties
) : GitRepositoryAdapter() {

	override fun getRepositories(project: String): List<Repository> {

		return bitBucketClient.getRepositories(project).values
			.map { repositoryEntity ->
				Repository(
					name = repositoryEntity.name,
					project = repositoryEntity.project.name,
					browseUrl = repositoryEntity.links.self.first().href,
					// FIXME: let the user define how to authenticate
					cloneUrl = repositoryEntity.links.clone.first { it.href.startsWith("http", true) }.href
				)
			}
	}

	override fun getCredentialProvider(): CredentialsProvider? {
		// FIXME: make it possible to authenticate using ssh keys. also make the username configurable
		return UsernamePasswordCredentialsProvider("username", bitBucketAdapterProperties.token)
	}
}
