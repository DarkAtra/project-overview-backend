package de.idealo.projectoverviewhackday.model

data class CheckResult<T>(
	val check: Check<T>,
	val checkOutcome: CheckOutcome,
	val checkStatus: CheckStatus
)
