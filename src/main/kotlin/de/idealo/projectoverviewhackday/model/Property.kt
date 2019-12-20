package de.idealo.projectoverviewhackday.model

data class Property(
	val key: String,
	val value: String
)

fun List<Property>.merge(other: List<Property>): List<Property> {
	val result = mutableListOf(*other.toTypedArray())
	this.stream().filter { !result.contains(it) }.forEach { result.add(it) }
	return listOf(*result.toTypedArray())
}
