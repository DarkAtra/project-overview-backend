package de.idealo.projectoverviewhackday.model

class DependencyCheck(
	expectedArtifact: Artifact,
	override val id: Long,
	override val name: String,
	override val required: Boolean
) : ArtifactCheck(expectedArtifact) {

	override fun getArtifact(repository: Repository): Artifact? {

		return repository.dependencies.find { it.groupId == expectedArtifact.groupId && it.artifactId == expectedArtifact.artifactId }
	}
}
