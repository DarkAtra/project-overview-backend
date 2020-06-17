package de.idealo.projectoverviewhackday.clients.repository.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class RepositoryEntity(

	@Id
	val name: String,

	val browseUrl: String,

	val cloneUrl: String
)
