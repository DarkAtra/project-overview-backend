package de.idealo.projectoverviewhackday.clients.bitbucket.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RepositoryEntity(
	@JsonProperty("slug")
	val id: String,
	val name: String,
	val project: ProjectEntity,
	val links: LinkEntity
)
