package de.idealo.projectoverviewhackday.model

data class ParentCheck(
	val artifact: Artifact,
	val versionEvaluator: VersionEvaluator = VersionEvaluator(deprecateThreshold = 2),
	override val displayName: String
) : Check {

	override fun check(repository: Repository): CheckOutcome {

		val parent = repository.parent ?: return CheckOutcome.NOT_PRESENT

		return artifact.version
			?.let { versionEvaluator.parse(it) }
			?.let { latestVersion ->
				parent.version
					?.let { versionEvaluator.parse(it) }
					?.let { versionEvaluator.evaluate(it, latestVersion) }
					?: return CheckOutcome.UNKNOWN
			}
			?: CheckOutcome.UNKNOWN
	}
}
