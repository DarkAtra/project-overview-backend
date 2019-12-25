package de.idealo.projectoverviewhackday.clients.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ProjectEntity(
	val id: Long,
	@JsonProperty("key")
	val name: String
)
