package de.idealo.projectoverviewhackday.model

class OpenshiftPropertyCheck(
	private val expectedProperty: Property,
	override val id: Long,
	override val name: String,
	override val required: Boolean
) : Check<Property> {

	override fun check(repository: Repository): CheckResult<Property> {

		val foundProperty = repository.openShiftProperties.find { it.key == expectedProperty.key }
		val checkOutcome = expectedProperty.compare(foundProperty)
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
