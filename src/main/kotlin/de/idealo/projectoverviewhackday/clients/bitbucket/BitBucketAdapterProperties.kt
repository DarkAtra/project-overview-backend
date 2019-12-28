package de.idealo.projectoverviewhackday.clients.bitbucket

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import java.net.URI
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Validated
@ConfigurationProperties("adapters.bit-bucket")
data class BitBucketAdapterProperties(
	@field:NotNull
	var url: URI?,
	@field:NotBlank
	var token: String?
)
