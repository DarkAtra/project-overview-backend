package de.darkatra.projectoverview.api.model

import javax.validation.constraints.NotBlank

data class RepositoryDeleteRequest(
	@field:NotBlank
	val name: String
)
