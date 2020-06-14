package de.idealo.projectoverviewhackday.clients.repository

import de.idealo.projectoverviewhackday.clients.repository.model.RepositoryEntity
import de.idealo.projectoverviewhackday.modules.repository.service.model.Repository
import org.springframework.stereotype.Component

@Component
class RepositoryEntityMapper {

	fun map(repositoryEntity: RepositoryEntity): Repository {

		val (name, browseUrl, cloneUrl) = repositoryEntity
		return Repository(
			name = name,
			browseUrl = browseUrl,
			cloneUrl = cloneUrl
		)
	}

	fun map(repository: Repository): RepositoryEntity {

		val (name, browseUrl, cloneUrl) = repository
		return RepositoryEntity(
			name = name,
			browseUrl = browseUrl,
			cloneUrl = cloneUrl
		)
	}
}
