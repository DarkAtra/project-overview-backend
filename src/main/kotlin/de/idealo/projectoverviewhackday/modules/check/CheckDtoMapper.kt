package de.idealo.projectoverviewhackday.modules.check

import de.idealo.projectoverviewhackday.modules.check.model.CheckDto
import de.idealo.projectoverviewhackday.modules.check.service.model.Check
import org.springframework.stereotype.Component

@Component
class CheckDtoMapper {

	fun map(check: Check): CheckDto {

		val (name) = check
		return CheckDto(
			name = name
		)
	}
}
