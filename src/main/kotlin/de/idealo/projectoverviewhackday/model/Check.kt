package de.idealo.projectoverviewhackday.model

interface Check {

	val displayName: String

	fun check(repository: Repository): CheckOutcome
}
