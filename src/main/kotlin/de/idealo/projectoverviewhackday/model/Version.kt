package de.idealo.projectoverviewhackday.model

data class Version(
	val major: Long,
	val minor: Long,
	val build: Long
) {
	fun compare(other: Version?): CheckOutcome {

		return when {
			other == null -> CheckOutcome.NOT_FOUND
			major - other.major > 1 -> CheckOutcome.VERY_OUTDATED
			major - other.major > 0 || minor - other.minor > 0 || build - other.build > 0 -> CheckOutcome.OUTDATED
			else -> CheckOutcome.UP_TO_DATE
		}
	}
}
