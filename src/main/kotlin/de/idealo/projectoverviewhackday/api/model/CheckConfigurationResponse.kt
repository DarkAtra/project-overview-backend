package de.idealo.projectoverviewhackday.api.model

data class CheckConfigurationResponse(
	val name: String,
	val type: String,
	val additionalProperties: Map<String, String>
)
