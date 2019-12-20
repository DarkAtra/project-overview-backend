package de.idealo.projectoverviewhackday.model

data class DependencyCheck(
	val artifact: Artifact,
	val versionEvaluator: VersionEvaluator = VersionEvaluator(),
	override val displayName: String
) : Check {

	override fun check(repository: Repository): CheckOutcome {

		val dependency = repository.dependencies
			.find { it.groupId == artifact.groupId && it.artifactId == artifact.artifactId }
			?: return CheckOutcome.NOT_PRESENT

		return artifact.version
			?.let { versionEvaluator.parse(it) }
			?.let { latestVersion ->
				dependency.version
					?.let { versionEvaluator.parse(it) }
					?.let { versionEvaluator.evaluate(it, latestVersion) }
					?: return CheckOutcome.UNKNOWN
			}
			?: CheckOutcome.UNKNOWN
	}
}
