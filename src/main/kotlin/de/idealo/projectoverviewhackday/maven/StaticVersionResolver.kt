package de.idealo.projectoverviewhackday.maven

import de.idealo.projectoverviewhackday.base.model.Version

class StaticVersionResolver(
	private val version: Version
) : VersionResolver {

	override fun resolve(groupId: String, artifactId: String) = version
}
