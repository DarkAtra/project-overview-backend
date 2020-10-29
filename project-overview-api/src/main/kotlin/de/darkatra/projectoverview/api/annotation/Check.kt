package de.darkatra.projectoverview.api.annotation

import org.springframework.stereotype.Component

@Component
@MustBeDocumented
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Check(
	val type: String
)
