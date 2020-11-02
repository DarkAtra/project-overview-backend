package de.darkatra.projectoverview.context

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties
@Suppress("ConfigurationProperties")
data class PluginProperties(
	val plugins: Map<String, Map<String, Any>>
)
