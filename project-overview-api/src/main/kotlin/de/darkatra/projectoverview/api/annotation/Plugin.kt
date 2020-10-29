package de.darkatra.projectoverview.api.annotation

@MustBeDocumented
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Plugin(
	val name: String,
	val author: String
)
