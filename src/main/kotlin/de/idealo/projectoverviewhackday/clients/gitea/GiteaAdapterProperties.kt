package de.idealo.projectoverviewhackday.clients.gitea

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.net.URI
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@ConfigurationProperties("adapters.gitea")
data class GiteaAdapterProperties(
	@field:NotNull
	var url: URI?,
	@field:NotBlank
	var token: String?
)
