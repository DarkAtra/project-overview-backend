package de.idealo.projectoverviewhackday.clients.check.model

import java.io.Serializable
import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class CheckToRepositoryIdEntity(
	@Column(nullable = false)
	val checkId: String,
	@Column(nullable = false)
	val repositoryId: String
) : Serializable
