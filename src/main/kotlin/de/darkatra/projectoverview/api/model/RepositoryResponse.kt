package de.darkatra.projectoverview.api.model

data class RepositoryResponse(
	val name: String,
	val browseUrl: String,
	val cloneUrl: String,
	val checks: List<String>
)
