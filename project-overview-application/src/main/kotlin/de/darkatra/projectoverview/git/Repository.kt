package de.darkatra.projectoverview.git

import de.darkatra.projectoverview.toSlug

data class Repository(
	val name: String,
	val browseUrl: String,
	val cloneUrl: String
) {
	val slug: String
		get() = name.toSlug()
}
