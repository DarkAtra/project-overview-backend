package de.idealo.projectoverviewhackday.model

import java.util.regex.Pattern

class VersionEvaluator(
	private val deprecateThreshold: Long = 1L,
	private val outdatedThreshold: Long = 1L
) {

	private val pattern = Pattern.compile("([0-9]+)\\.([0-9]+)\\.([0-9]+)")

	fun evaluate(version: Version, latestVersion: Version): CheckOutcome {
		val majorDiff = latestVersion.major - version.major
		val minorDiff = latestVersion.minor - version.minor
		return when {
			latestVersion == version -> CheckOutcome.UP_TO_DATE
			majorDiff >= deprecateThreshold -> CheckOutcome.DEPRECATED
			majorDiff > 0 || minorDiff >= outdatedThreshold -> CheckOutcome.OUTDATED
			else -> CheckOutcome.UNKNOWN
		}
	}

	fun parse(version: String): Version? {

		return pattern.matcher(version).results().findFirst()
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
