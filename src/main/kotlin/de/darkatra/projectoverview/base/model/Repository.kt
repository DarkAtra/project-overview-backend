package de.darkatra.projectoverview.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.annotation.ReadOnlyProperty
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Repository(

	@Id
	val name: String,

	val browseUrl: String,

	val cloneUrl: String,

	@ReadOnlyProperty
	var checks: List<String> = emptyList()
)
