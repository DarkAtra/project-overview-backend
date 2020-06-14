package de.idealo.projectoverviewhackday.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@ConstructorBinding
@ConfigurationProperties("services.repository")
data class RepositoryServiceProperties(

	@NotBlank
	var project: String,

	@NotBlank
	var dir: String
)