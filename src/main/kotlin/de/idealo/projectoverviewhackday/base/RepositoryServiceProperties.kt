package de.idealo.projectoverviewhackday.base

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.nio.file.Path
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "projectoverview.repository")
data class RepositoryServiceProperties(
	@NotNull
	val dir: Path,
	// TODO: should be configurable per repository
	@NotBlank
	val username: String,
	// TODO: should be configurable per repository
	@NotBlank
	val password: String
)
