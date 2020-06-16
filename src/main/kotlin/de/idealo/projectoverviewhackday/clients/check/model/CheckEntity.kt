package de.idealo.projectoverviewhackday.clients.check.model

import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class CheckEntity(
	@Id
	val name: String
)
