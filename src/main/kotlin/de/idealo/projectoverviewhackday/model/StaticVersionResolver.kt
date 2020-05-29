package de.idealo.projectoverviewhackday.model

class StaticVersionResolver(
	private val version: Version
) : VersionResolver {

	override fun resolve(groupId: String, artifactId: String) = version
}
