package de.idealo.projectoverviewhackday.clients.bitbucket.model

data class LinkEntity(
	val clone: List<UrlEntity> = emptyList(),
	val self: List<UrlEntity> = emptyList()
)