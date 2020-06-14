package de.idealo.projectoverviewhackday.clients.gitea

import feign.Feign
import feign.Request
import feign.RequestInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile
import java.util.concurrent.TimeUnit

@Configuration
@Profile("gitea")
@Import(FeignClientsConfiguration::class)
@EnableConfigurationProperties(GiteaAdapterProperties::class)
class GiteaAdapterConfiguration(private val giteaAdapterProperties: GiteaAdapterProperties) {

	@Bean
	fun giteaRepositoryAdapter(giteaClient: GiteaClient): GiteaRepositoryAdapter {

		return GiteaRepositoryAdapter(
			giteaClient = giteaClient
		)
	}

	@Bean
	fun giteaClient(feignEncoder: Encoder,
	                feignDecoder: Decoder,
	                giteaClientAuthenticationInterceptor: RequestInterceptor): GiteaClient {

		return Feign.builder()
			.options(Request.Options(
				giteaAdapterProperties.connectTimeoutMillis,
				TimeUnit.MILLISECONDS,
				giteaAdapterProperties.readTimeoutMillis,
				TimeUnit.MILLISECONDS,
				true
			))
			.encoder(feignEncoder)
			.decoder(feignDecoder)
			.decode404()
			.requestInterceptor(giteaClientAuthenticationInterceptor)
			.target(GiteaClient::class.java, giteaAdapterProperties.url.toString())
	}

	@Bean
	fun giteaClientAuthenticationInterceptor(): RequestInterceptor {

		return RequestInterceptor {
			it.header("Authorization", "Bearer ${giteaAdapterProperties.token}")
		}
	}
}
