package de.idealo.projectoverviewhackday.base.model

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Check(
	val type: String
)
