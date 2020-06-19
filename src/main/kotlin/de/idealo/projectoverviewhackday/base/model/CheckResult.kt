package de.idealo.projectoverviewhackday.base.model

data class CheckResult(
	val status: Status,
	val message: String
) {

	enum class Status {
		SUCCESSFUL,
		FAILED,
		ABORTED,
		SKIPPED
	}
}
