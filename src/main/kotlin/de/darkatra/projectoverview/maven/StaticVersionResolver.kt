package de.darkatra.projectoverview.maven

class StaticVersionResolver(
	private val version: String
) : VersionResolver {

	override fun resolve(groupId: String, artifactId: String) = version
}
