package de.idealo.projectoverviewhackday.model

abstract class ArtifactCheck(
	protected val expectedArtifact: Artifact
) : Check<Artifact> {

	protected abstract fun getArtifact(repository: Repository): Artifact?

	override fun check(repository: Repository): CheckResult<Artifact> {
		return CheckResult(
			check = this,
			expected = expectedArtifact,
			found = getArtifact(repository),
			required = required
		)
	}
}
