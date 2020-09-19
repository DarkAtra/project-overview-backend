package de.darkatra.projectoverview.api.model

// TODO: validation
data class RepositoryCreateRequest(
	val name: String,
	val browseUrl: String,
	val cloneUrl: String,
	val checks: List<String>
)
