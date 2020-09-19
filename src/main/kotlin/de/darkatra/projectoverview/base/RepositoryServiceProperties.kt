package de.darkatra.projectoverview.base

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.nio.file.Path
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "projectoverview.repository")
data class RepositoryServiceProperties(
	@NotNull
	val dir: Path
)
