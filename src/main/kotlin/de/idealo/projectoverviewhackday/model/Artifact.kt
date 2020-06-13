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

	fun compare(wantedArtifact: Artifact?): CheckOutcome {

		return when {
			wantedArtifact == null -> CheckOutcome.NOT_FOUND
			version == null -> CheckOutcome.UNKNOWN
			groupId == wantedArtifact.groupId && artifactId == wantedArtifact.artifactId -> version.compare(wantedArtifact.version)
			else -> CheckOutcome.NOT_FOUND
		}
	}
}
