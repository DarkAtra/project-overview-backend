package de.idealo.projectoverviewhackday.model

data class OpenShiftPropertyCheck(
	val property: Property,
	override val displayName: String
) : Check {

	override fun check(repository: Repository): CheckOutcome {

		return repository.openShiftProperties
			.find { it.key == property.key }
			?.let {
				when (it.value) {
					property.value -> CheckOutcome.UP_TO_DATE
					else -> CheckOutcome.OUTDATED
				}
			}
			?: CheckOutcome.NOT_PRESENT
	}
}
