package de.darkatra.projectoverview.context

import org.springframework.context.support.AbstractApplicationContext
import java.util.Properties

data class Plugin(
	val applicationContext: AbstractApplicationContext,
	val properties: Properties,
	val name: String,
	val author: String
)
