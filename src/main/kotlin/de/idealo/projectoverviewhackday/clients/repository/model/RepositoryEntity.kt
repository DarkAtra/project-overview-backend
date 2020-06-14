package de.idealo.projectoverviewhackday.clients.repository.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id

@Entity
data class RepositoryEntity(
	@Id
	val name: String,
	@Column(unique = true, nullable = false)
	val browseUrl: String,
	@Column(unique = true, nullable = false)
	val cloneUrl: String
)
