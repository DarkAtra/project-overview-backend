package de.darkatra.projectoverview.context

import org.springframework.util.LinkedCaseInsensitiveMap
import java.nio.file.Path

open class DefaultCheckContext(
	private val checkName: String,
	private val repositoryDirectory: Path,
	private val parameters: LinkedCaseInsensitiveMap<String>,
) : CheckContext {

	override fun getCheckName(): String = checkName

	override fun getRepositoryDirectory(): Path = repositoryDirectory

	override fun getParameters(): LinkedCaseInsensitiveMap<String> = parameters
}
