package de.idealo.projectoverviewhackday.base.model

@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class Parameter(
	val name: String = "",
	val required: Boolean = true
)
