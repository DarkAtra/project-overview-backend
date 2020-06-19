package de.idealo.projectoverviewhackday.maven

import de.idealo.projectoverviewhackday.base.model.Version

interface VersionResolver {
	fun resolve(groupId: String, artifactId: String): Version
}
