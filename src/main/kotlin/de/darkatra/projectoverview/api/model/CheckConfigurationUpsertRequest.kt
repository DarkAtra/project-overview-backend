package de.darkatra.projectoverview.api.model

import javax.validation.constraints.NotBlank

data class CheckConfigurationUpsertRequest(
	@field:NotBlank
	val type: String?,
	val additionalProperties: Map<@NotBlank String, @NotBlank String>?
)