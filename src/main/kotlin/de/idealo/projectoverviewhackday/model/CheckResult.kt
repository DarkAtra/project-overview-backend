package de.idealo.projectoverviewhackday.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

data class CheckResult(
	@JsonIgnore
	val check: Check,
	val checkOutcome: CheckOutcome = CheckOutcome.NOT_CHECKED
) {
	@JsonProperty
	fun getName() = check.displayName
}
