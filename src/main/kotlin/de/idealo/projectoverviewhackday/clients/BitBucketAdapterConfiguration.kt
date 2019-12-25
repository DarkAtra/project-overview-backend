package de.idealo.projectoverviewhackday.clients

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

@Configuration
@Import(FeignClientsConfiguration::class)
@EnableConfigurationProperties(BitBucketAdapterProperties::class)
class BitBucketAdapterConfiguration(private val bitBucketAdapterProperties: BitBucketAdapterProperties) {

	@Bean
	fun bitBucketClient(client: Client,
						feignEncoder: Encoder,
						feignDecoder: Decoder,
						bitBucketClientAuthenticationInterceptor: RequestInterceptor): BitBucketClient {

		return Feign.builder()
			.client(client)
			.encoder(feignEncoder)
			.decoder(feignDecoder)
			.decode404()
			.requestInterceptor(bitBucketClientAuthenticationInterceptor)
			.target(BitBucketClient::class.java, bitBucketAdapterProperties.url!!)
	}

	@Bean
	fun bitBucketClientAuthenticationInterceptor(): RequestInterceptor {

		return RequestInterceptor {
			it.header("Authorization", "Bearer ${bitBucketAdapterProperties.token!!}")
		}
	}
}
