package de.idealo.projectoverviewhackday.model

import java.util.regex.Pattern

class VersionParser {

	private val pattern = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)")

	fun parse(version: String?): Version? {

		return version?.let {
			pattern.matcher(version).results().findFirst()
				.map { matchResult ->
					Version(
						major = matchResult.group(1).toLong(),
						minor = matchResult.group(2).toLong(),
						build = matchResult.group(3).toLong()
					)
				}
				.orElse(null)
		}
	}
}
