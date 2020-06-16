package de.idealo.projectoverviewhackday.modules.repository.model

data class RepositoryDto(
	val name: String,
	val browseUrl: String,
	val cloneUrl: String,
	val checks: List<String>
)
