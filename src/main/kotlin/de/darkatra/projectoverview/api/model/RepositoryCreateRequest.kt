package de.darkatra.projectoverview.api.model

import org.hibernate.validator.constraints.URL
import javax.validation.constraints.NotBlank

data class RepositoryCreateRequest(
	@field:NotBlank
	val name: String,
	@field:NotBlank
	@field:URL
	val browseUrl: String,
	@field:NotBlank
	@field:URL
	val cloneUrl: String
)
