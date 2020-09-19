package de.darkatra.projectoverview.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CheckConfiguration(

	@Id
	val name: String,

	val type: String,

	// TODO: allow complex types?
	val additionalProperties: Map<String, String>
)
