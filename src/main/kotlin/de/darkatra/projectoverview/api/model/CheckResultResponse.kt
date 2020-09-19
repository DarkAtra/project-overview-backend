package de.darkatra.projectoverview.api.model

data class CheckResultResponse(
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
