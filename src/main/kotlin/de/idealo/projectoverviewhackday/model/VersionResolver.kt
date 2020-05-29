package de.idealo.projectoverviewhackday.model

interface VersionResolver {
	fun resolve(groupId: String, artifactId: String): Version
}
