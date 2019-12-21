package de.idealo.projectoverviewhackday.model

class ParentCheck(
	expectedArtifact: Artifact,
	override val id: Long,
	override val name: String,
	override val required: Boolean
) : ArtifactCheck(expectedArtifact) {

	override fun getArtifact(repository: Repository): Artifact? = repository.parent
}
