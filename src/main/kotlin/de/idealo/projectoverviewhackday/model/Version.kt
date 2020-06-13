package de.idealo.projectoverviewhackday.model

data class Version(
	val major: Long,
	val minor: Long,
	val build: Long
) {
	fun compare(wantedVersion: Version?): CheckOutcome {

		return when {
			wantedVersion == null -> CheckOutcome.NOT_FOUND
			wantedVersion.major - major > 1 -> CheckOutcome.VERY_OUTDATED
			wantedVersion.major - major > 0 -> CheckOutcome.OUTDATED
			wantedVersion.minor - minor > 0 && wantedVersion.major - major >= 0 -> CheckOutcome.OUTDATED
			wantedVersion.build - build > 0 && wantedVersion.minor - minor >= 0 && wantedVersion.major - major >= 0 -> CheckOutcome.OUTDATED
			else -> CheckOutcome.UP_TO_DATE
		}
	}
}
