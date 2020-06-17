package de.idealo.projectoverviewhackday.clients.check.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CheckEntity(

	@Id
	val name: String,

	val checkType: CheckTypeEntity,

	val additionalProperties: List<CheckParameterEntity<Any>>
)
