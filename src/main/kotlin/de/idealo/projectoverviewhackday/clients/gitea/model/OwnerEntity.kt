package de.idealo.projectoverviewhackday.clients.gitea.model

import com.fasterxml.jackson.annotation.JsonProperty

data class OwnerEntity(
	@JsonProperty("username")
	val name: String
)
