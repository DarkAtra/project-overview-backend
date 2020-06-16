package de.idealo.projectoverviewhackday.modules.check.service

import de.idealo.projectoverviewhackday.clients.check.CheckAdapter
import de.idealo.projectoverviewhackday.clients.check.CheckEntityMapper
import de.idealo.projectoverviewhackday.modules.check.service.model.Check
import de.idealo.projectoverviewhackday.modules.repository.service.model.Repository
import org.springframework.stereotype.Service

@Service
class CheckService(
	private val checkAdapter: CheckAdapter,
	private val checkEntityMapper: CheckEntityMapper
) {


	fun getChecks(repository: Repository): List<Check> {

		return checkAdapter.findAll()
			.map { checkEntityMapper.map(it) }
	}
}
