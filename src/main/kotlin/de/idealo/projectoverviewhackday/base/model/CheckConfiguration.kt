package de.idealo.projectoverviewhackday.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CheckConfiguration(

	@Id
	val name: String,

	val type: String,

	val additionalProperties: Map<String, String>
)
