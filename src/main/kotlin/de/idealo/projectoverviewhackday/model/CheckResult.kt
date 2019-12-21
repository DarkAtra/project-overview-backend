package de.idealo.projectoverviewhackday.model

data class CheckResult<T>(
	val check: Check<T>,
	val found: T?,
	val expected: T,
	val required: Boolean
)
