package de.darkatra.projectoverview.context

import org.springframework.util.LinkedCaseInsensitiveMap
import java.nio.file.Path

class PluginAwareCheckContext(
	private val plugin: Plugin,
	checkName: String,
	repositoryDirectory: Path,
	parameters: LinkedCaseInsensitiveMap<String>,
) : DefaultCheckContext(checkName, repositoryDirectory, parameters), PluginAware {

	override fun getPlugin(): Plugin = plugin
}
