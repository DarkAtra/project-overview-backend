package de.idealo.projectoverviewhackday.model

import org.apache.maven.model.Dependency
import org.apache.maven.model.Parent

data class Artifact(
	val groupId: String,
	val artifactId: String,
	val version: Version?
) {
	companion object {
		fun of(parent: Parent, versionParser: VersionParser): Artifact {
			return Artifact(
				groupId = parent.groupId,
				artifactId = parent.artifactId,
				version = versionParser.parse(parent.version)
			)
		}

		fun of(dependency: Dependency, versionParser: VersionParser): Artifact {
			return Artifact(
				groupId = dependency.groupId,
				artifactId = dependency.artifactId,
				version = versionParser.parse(dependency.version)
			)
		}
	}

	fun compare(other: Artifact?): CheckOutcome {
		return when {
			other == null -> CheckOutcome.NOT_FOUND
			version == null -> CheckOutcome.UNKNOWN
			groupId == other.groupId && artifactId == other.artifactId -> version.compare(other.version)
			else -> CheckOutcome.NOT_FOUND
		}
	}
}
