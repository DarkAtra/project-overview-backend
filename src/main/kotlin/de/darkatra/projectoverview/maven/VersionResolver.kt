package de.darkatra.projectoverview.maven

interface VersionResolver {
	fun resolve(groupId: String, artifactId: String): String
}
