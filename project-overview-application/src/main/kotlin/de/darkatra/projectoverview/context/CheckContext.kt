package de.darkatra.projectoverview.context

import org.springframework.util.LinkedCaseInsensitiveMap

data class CheckContext(
	val checkName: String,
	val parameters: LinkedCaseInsensitiveMap<String>
)
