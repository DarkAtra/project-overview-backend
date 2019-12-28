package de.idealo.projectoverviewhackday.clients.gitea

import de.idealo.projectoverviewhackday.clients.common.MavenPomParser
import de.idealo.projectoverviewhackday.clients.common.OpenShiftPropertyParser
import feign.Client
import feign.Feign
import feign.RequestInterceptor
import feign.codec.Decoder
import feign.codec.Encoder
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Profile

@Configuration
@Profile("gitea")
@Import(FeignClientsConfiguration::class)
@EnableConfigurationProperties(GiteaAdapterProperties::class)
class GiteaAdapterConfiguration(private val giteaAdapterProperties: GiteaAdapterProperties) {

	@Bean
	fun giteaRepositoryAdapter(giteaClient: GiteaClient,
							   giteaAdapterProperties: GiteaAdapterProperties,
							   mavenPomParser: MavenPomParser,
							   openShiftPropertyParser: OpenShiftPropertyParser): GiteaRepositoryAdapter {

		return GiteaRepositoryAdapter(
			giteaClient = giteaClient,
			giteaAdapterProperties = giteaAdapterProperties,
			mavenPomParser = mavenPomParser,
			openShiftPropertyParser = openShiftPropertyParser
		)
	}

	@Bean
	fun giteaClient(client: Client,
					feignEncoder: Encoder,
					feignDecoder: Decoder,
					giteaClientAuthenticationInterceptor: RequestInterceptor): GiteaClient {

		return Feign.builder()
			.client(client)
			.encoder(feignEncoder)
			.decoder(feignDecoder)
			.decode404()
			.requestInterceptor(giteaClientAuthenticationInterceptor)
			.target(GiteaClient::class.java, giteaAdapterProperties.url!!.toString())
	}

	@Bean
	fun giteaClientAuthenticationInterceptor(): RequestInterceptor {

		return RequestInterceptor {
			it.header("Authorization", "Bearer ${giteaAdapterProperties.token!!}")
		}
	}
}
