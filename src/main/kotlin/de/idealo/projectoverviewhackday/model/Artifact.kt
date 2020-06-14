package de.idealo.projectoverviewhackday.model

data class Artifact(
	val groupId: String,
	val artifactId: String,
	val version: Version,
	val scope: String
)