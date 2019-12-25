package de.idealo.projectoverviewhackday.clients

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.constraints.NotBlank

@Validated
@ConfigurationProperties("adapters.bit-bucket")
data class BitBucketAdapterProperties(
	@field:NotBlank
	var url: String?,
	@field:NotBlank
	var token: String?
)
