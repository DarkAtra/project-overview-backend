package de.idealo.projectoverviewhackday.clients.gitea.model

data class RepositoryEntity(
	val name: String,
	val owner: OwnerEntity
)
