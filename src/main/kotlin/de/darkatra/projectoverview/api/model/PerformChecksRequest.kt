package de.darkatra.projectoverview.api.model

import javax.validation.constraints.NotBlank

data class PerformChecksRequest(
	@field:NotBlank
	val repositoryName: String
)
