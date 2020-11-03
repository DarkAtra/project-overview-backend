package de.darkatra.projectoverview.check

import de.darkatra.projectoverview.toSlug
import org.springframework.util.LinkedCaseInsensitiveMap

data class CheckConfiguration(
	val name: String,
	val properties: LinkedCaseInsensitiveMap<String>
) {
	val slug: String
		get() = name.toSlug()
}
