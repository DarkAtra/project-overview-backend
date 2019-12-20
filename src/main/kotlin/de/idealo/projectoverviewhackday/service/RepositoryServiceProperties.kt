package de.idealo.projectoverviewhackday.service

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@ConfigurationProperties("services.repository")
data class RepositoryServiceProperties(
	@field:NotBlank
	var project: String?,
	@field:NotNull
	var excludedRepositories: List<String> = emptyList()
)
