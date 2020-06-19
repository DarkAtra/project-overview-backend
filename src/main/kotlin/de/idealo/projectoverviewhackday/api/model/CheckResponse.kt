package de.idealo.projectoverviewhackday.api.model

data class CheckResponse(
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
