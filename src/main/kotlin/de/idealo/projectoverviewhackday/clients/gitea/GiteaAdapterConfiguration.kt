package de.idealo.projectoverviewhackday.clients.gitea

import feign.Feign
import feign.Request
import feign.Target
import feign.codec.Decoder
import feign.codec.Encoder
import org.springframework.cloud.openfeign.FeignClientsConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import java.util.concurrent.TimeUnit

@Configuration
@Import(FeignClientsConfiguration::class)
class GiteaAdapterConfiguration {

	@Bean
	fun giteaRepositoryAdapter(giteaClient: GiteaClient): GiteaRepositoryAdapter {

		return GiteaRepositoryAdapter(
			giteaClient = giteaClient
		)
	}

	@Bean
	fun giteaClient(feignEncoder: Encoder,
					feignDecoder: Decoder): GiteaClient {

		return Feign.builder()
			.options(Request.Options(
				10, TimeUnit.SECONDS,
				1, TimeUnit.MINUTES,
				true
			))
			.encoder(feignEncoder)
			.decoder(feignDecoder)
			.decode404()
			.target(Target.EmptyTarget.create(GiteaClient::class.java))
	}
}
