package de.idealo.projectoverviewhackday.model

interface Check<T> {
	val id: Long
	val name: String
	val required: Boolean
	fun check(repository: Repository): CheckResult<T>
}
