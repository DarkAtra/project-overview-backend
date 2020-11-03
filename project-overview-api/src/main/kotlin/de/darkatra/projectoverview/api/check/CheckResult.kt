package de.darkatra.projectoverview.api.check

import java.time.Instant

data class CheckResult(
	val checkOutcome: CheckOutcome,
	val message: String, // TODO: is a generic message enough or should there also be a way to provide additional information (e.g. for failing checks)
	var checked: Instant? = null
)
