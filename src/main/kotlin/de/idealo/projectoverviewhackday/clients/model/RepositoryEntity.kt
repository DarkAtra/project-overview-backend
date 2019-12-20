package de.idealo.projectoverviewhackday.clients.model

data class RepositoryEntity(
	val slug: String,
	val id: Long,
	val name: String,
	val project: ProjectEntity
)
