package de.idealo.projectoverviewhackday.model

import com.fasterxml.jackson.annotation.JsonIgnore

data class CheckResult<T>(
	val check: Check<T>,
	@JsonIgnore
	val found: T?,
	@JsonIgnore
	val expected: T,
	@JsonIgnore
	val required: Boolean,
	val checkOutcome: CheckOutcome,
	val checkStatus: CheckStatus
)
