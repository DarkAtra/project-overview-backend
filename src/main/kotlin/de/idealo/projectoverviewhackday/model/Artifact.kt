package de.idealo.projectoverviewhackday.model

import org.apache.maven.model.Dependency
import org.apache.maven.model.Parent

data class Artifact(
	val groupId: String,
	val artifactId: String,
	val version: String?
) {
	companion object {
		fun of(parent: Parent): Artifact {
			return Artifact(
				groupId = parent.groupId,
				artifactId = parent.artifactId,
				version = parent.version
			)
		}

		fun of(dependency: Dependency): Artifact {
			return Artifact(
				groupId = dependency.groupId,
				artifactId = dependency.artifactId,
				version = dependency.version
			)
		}
	}
}
