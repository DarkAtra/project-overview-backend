package de.darkatra.projectoverview.git

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.nio.file.Path

@ConstructorBinding
@ConfigurationProperties("projectoverview.repository")
data class RepositoryServiceProperties(
	val dir: Path
)
