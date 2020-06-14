package de.idealo.projectoverviewhackday.modules.repository.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "projectoverview", value = "repository")
data class RepositoryServiceProperties(
	@NotBlank
	val dir: String
)
