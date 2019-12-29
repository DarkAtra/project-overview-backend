package de.idealo.projectoverviewhackday.model

data class Property(
	val key: String,
	val value: String
) {

	fun compare(other: Property?): CheckOutcome {

		return when {
			other == null -> CheckOutcome.NOT_FOUND
			key == other.key -> if (value == other.value) CheckOutcome.UP_TO_DATE else CheckOutcome.OUTDATED
			else -> CheckOutcome.NOT_FOUND
		}
	}
}

fun List<Property>.merge(other: List<Property>): List<Property> {

	val result = mutableListOf(*other.toTypedArray())
	this.stream().filter { !result.contains(it) }.forEach { result.add(it) }
	return listOf(*result.toTypedArray())
}
