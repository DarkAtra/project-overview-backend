package de.idealo.projectoverviewhackday.base.model

import java.time.Instant

data class CheckResult(
	var checkName: String,
	val status: Status,
	val message: String,
	var createdDate: Instant? = null
) {

	enum class Status {
		SUCCESSFUL,
		FAILED,
		ABORTED,
		SKIPPED
	}
}
