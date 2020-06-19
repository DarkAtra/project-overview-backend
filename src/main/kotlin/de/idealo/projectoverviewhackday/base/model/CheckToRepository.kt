package de.idealo.projectoverviewhackday.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CheckToRepository(
	@Id
	val id: CheckToRepositoryId
) {

	data class CheckToRepositoryId(
		val checkId: String,
		val repositoryId: String
	)
}
