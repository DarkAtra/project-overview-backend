package de.idealo.projectoverviewhackday.service

import de.idealo.projectoverviewhackday.model.Property
import org.springframework.stereotype.Component

@Component
class OpenShiftPropertyParser {

	fun parse(openShiftProperties: String): List<Property> {
		return openShiftProperties.lines()
			.mapNotNull {
				val split = it.split("=")
				when {
					split.size != 2 -> null
					else -> Property(
						key = split[0],
						value = split[1]
					)
				}
			}
	}
}
