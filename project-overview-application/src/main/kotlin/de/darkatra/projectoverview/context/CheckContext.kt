package de.darkatra.projectoverview.context

import org.springframework.util.LinkedCaseInsensitiveMap
import java.nio.file.Path

interface CheckContext {
	fun getCheckName(): String
	fun getRepositoryDirectory(): Path
	fun getParameters(): LinkedCaseInsensitiveMap<String>
}
