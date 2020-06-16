package de.idealo.projectoverviewhackday.clients.check

import de.idealo.projectoverviewhackday.clients.check.model.CheckEntity
import de.idealo.projectoverviewhackday.modules.check.service.model.Check
import org.springframework.stereotype.Component

@Component
class CheckEntityMapper {

	fun map(checkEntity: CheckEntity): Check {

		val (name) = checkEntity
		return Check(
			name = name
		)
	}
}
