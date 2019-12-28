package de.idealo.projectoverviewhackday.clients.bitbucket.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ProjectEntity(
	@JsonProperty("key")
	val name: String
)
