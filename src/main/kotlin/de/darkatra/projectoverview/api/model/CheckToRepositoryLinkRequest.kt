package de.darkatra.projectoverview.api.model

import javax.validation.constraints.NotBlank

data class CheckToRepositoryLinkRequest(
	@field:NotBlank
	val repositoryName: String
)