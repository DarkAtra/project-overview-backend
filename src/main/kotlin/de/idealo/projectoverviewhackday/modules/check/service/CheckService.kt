package de.idealo.projectoverviewhackday.modules.check.service

import de.idealo.projectoverviewhackday.clients.check.CheckAdapter
import de.idealo.projectoverviewhackday.clients.check.CheckEntityMapper
import de.idealo.projectoverviewhackday.modules.check.service.model.Check
import de.idealo.projectoverviewhackday.modules.repository.service.RepositoryService
import org.springframework.stereotype.Service

@Service
class CheckService(
	private val checkAdapter: CheckAdapter,
	private val checkEntityMapper: CheckEntityMapper,
	private val repositoryService: RepositoryService
) {

	fun getChecks(): List<Check> {

		return checkAdapter.findAll()
			.map { checkEntityMapper.map(it) }
	}

	fun performChecks(repositoryName: String) {

		repositoryService.ensureLocalRepositoryIsUpToDate(repositoryService.getRepository(repositoryName))
	}
}
