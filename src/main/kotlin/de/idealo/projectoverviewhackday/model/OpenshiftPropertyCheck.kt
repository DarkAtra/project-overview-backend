package de.idealo.projectoverviewhackday.model

class OpenshiftPropertyCheck(
	private val expectedProperty: Property,
	override val id: Long,
	override val name: String,
	override val required: Boolean
) : Check<Property> {

	override fun check(repository: Repository): CheckResult<Property> {

		return CheckResult(
			check = this,
			expected = expectedProperty,
			found = repository.openShiftProperties.find { it.key == expectedProperty.key },
			required = required
		)
	}
}
