package de.idealo.projectoverviewhackday.maven

interface VersionResolver {
	fun resolve(groupId: String, artifactId: String): String
}
