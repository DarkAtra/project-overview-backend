package de.idealo.projectoverviewhackday.clients.check.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CheckToRepositoryEntity(

	@Id
	val id: CheckToRepositoryIdEntity
)
