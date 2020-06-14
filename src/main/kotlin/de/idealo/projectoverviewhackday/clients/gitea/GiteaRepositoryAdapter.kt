package de.idealo.projectoverviewhackday.clients.gitea

import de.idealo.projectoverviewhackday.clients.common.AbstractGitRepositoryAdapter
import de.idealo.projectoverviewhackday.model.Repository

open class GiteaRepositoryAdapter(private val giteaClient: GiteaClient) : AbstractGitRepositoryAdapter() {

	override fun getRepositories(project: String): List<Repository> {

		return giteaClient.getRepositories(project)
			.orElse(emptyList())
			.map { repositoryEntity ->
				Repository(
					name = repositoryEntity.name,
					project = repositoryEntity.owner.name,
					browseUrl = "", // FIXME
					cloneUrl = "" // FIXME
				)
			}
	}
}
