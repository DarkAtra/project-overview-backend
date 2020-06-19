package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.Version
import de.idealo.projectoverviewhackday.base.model.VersionFormatException
import java.util.regex.Pattern

class VersionParser {

	private val pattern = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)")

	fun parse(version: String): Version {

		return pattern.matcher(version).results().findFirst()
			.map { matchResult ->
				Version(
					major = matchResult.group(1).toLong(),
					minor = matchResult.group(2).toLong(),
					build = matchResult.group(3).toLong()
				)
			}.orElseThrow { VersionFormatException("Could not parse Version from string '$version'.") }
	}
}
