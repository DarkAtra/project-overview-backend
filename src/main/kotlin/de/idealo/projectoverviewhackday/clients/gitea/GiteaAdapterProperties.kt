package de.idealo.projectoverviewhackday.clients.gitea

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.net.URI
import java.time.Duration
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@Validated
@ConfigurationProperties("adapters.gitea")
data class GiteaAdapterProperties(
	@field:NotNull
	var url: URI?,
	@field:NotBlank
	var token: String?,
	@field:Positive
	var connectTimeoutMillis: Long = Duration.ofSeconds(10).toMillis(),
	@field:Positive
	var readTimeoutMillis: Long = Duration.ofMinutes(1).toMillis()
)
