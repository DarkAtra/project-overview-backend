package de.idealo.projectoverviewhackday.clients.check.model

import javax.persistence.EmbeddedId
import javax.persistence.Entity

@Entity
data class CheckToRepositoryEntity(
	@EmbeddedId
	val id: CheckToRepositoryIdEntity
)
