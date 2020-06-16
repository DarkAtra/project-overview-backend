package de.idealo.projectoverviewhackday.modules.repository

import de.idealo.projectoverviewhackday.modules.repository.model.RepositoryDto
import de.idealo.projectoverviewhackday.modules.repository.service.model.Repository
import org.springframework.stereotype.Component

@Component
class RepositoryDtoMapper {

	fun map(repositoryDto: RepositoryDto): Repository {

		val (name, browseUrl, cloneUrl, _) = repositoryDto
		return Repository(
			name = name,
			browseUrl = browseUrl,
			cloneUrl = cloneUrl,
			checks = emptyList()
		)
	}

	fun map(repository: Repository): RepositoryDto {

		val (name, browseUrl, cloneUrl, checks) = repository
		return RepositoryDto(
			name = name,
			browseUrl = browseUrl,
			cloneUrl = cloneUrl,
			checks = checks
		)
	}
}
