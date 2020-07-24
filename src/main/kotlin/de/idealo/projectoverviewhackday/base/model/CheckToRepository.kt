package de.idealo.projectoverviewhackday.base.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class CheckToRepository(
	@Id
	val id: CheckToRepositoryId,

	val checkResults: MutableList<CheckResult> = mutableListOf()
) {

	data class CheckToRepositoryId(
		val checkId: String,
		val repositoryId: String
	)
}
