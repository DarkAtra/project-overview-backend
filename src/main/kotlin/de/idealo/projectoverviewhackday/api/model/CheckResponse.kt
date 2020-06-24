package de.idealo.projectoverviewhackday.api.model

data class CheckResponse(
	val checkName: String,
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
