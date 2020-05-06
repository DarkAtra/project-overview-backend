package de.idealo.projectoverviewhackday.model


abstract class ArtifactCheck(
	protected val expectedArtifact: Artifact
) : Check<Artifact> {

	protected abstract fun getArtifact(repository: Repository): Artifact?

	override fun check(repository: Repository): CheckResult<Artifact> {

		val foundArtifact = getArtifact(repository)
		val checkOutcome = expectedArtifact.compare(foundArtifact)
		return CheckResult(
			check = this,
			checkOutcome = checkOutcome,
			checkStatus = getCheckStatus(checkOutcome, required)
		)
	}

	private fun getCheckStatus(checkOutcome: CheckOutcome, required: Boolean): CheckStatus {

		return when (checkOutcome) {
			CheckOutcome.UP_TO_DATE -> CheckStatus.SUCCESS
			CheckOutcome.OUTDATED -> CheckStatus.WARNING
			CheckOutcome.VERY_OUTDATED -> CheckStatus.DANGER
			CheckOutcome.NOT_FOUND -> if (required) CheckStatus.DANGER else CheckStatus.UNNECESSARY
			else -> CheckStatus.UNNECESSARY
		}
	}
}
