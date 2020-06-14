package de.idealo.projectoverviewhackday.clients.bitbucket

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
class BitBucketAdapterConfiguration {

	@Bean
	fun bitBucketRepositoryAdapter(bitBucketClient: BitBucketClient): BitBucketRepositoryAdapter {

		return BitBucketRepositoryAdapter(
			bitBucketClient = bitBucketClient
		)
	}

	@Bean
	fun bitBucketClient(feignEncoder: Encoder,
						feignDecoder: Decoder): BitBucketClient {

		return Feign.builder()
			.options(Request.Options(
				10, TimeUnit.SECONDS,
				1, TimeUnit.MINUTES,
				true
			))
			.encoder(feignEncoder)
			.decoder(feignDecoder)
			.decode404()
			.target(Target.EmptyTarget.create(BitBucketClient::class.java))
	}
}
