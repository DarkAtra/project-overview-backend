package de.darkatra.projectoverview.context

import org.springframework.context.support.AbstractApplicationContext

data class Plugin(
	val applicationContext: AbstractApplicationContext,
	val packageName: String,
	val bean: Any,
	val name: String,
	val author: String
)
