package de.idealo.projectoverviewhackday.clients.bitbucket

import de.idealo.projectoverviewhackday.clients.common.MavenPomParser
import de.idealo.projectoverviewhackday.clients.common.OpenShiftPropertyParser
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

@Configuration
@Profile("bitbucket")
@Import(FeignClientsConfiguration::class)
@EnableConfigurationProperties(BitBucketAdapterProperties::class)
class BitBucketAdapterConfiguration(private val bitBucketAdapterProperties: BitBucketAdapterProperties) {

	@Bean
	fun bitBucketRepositoryAdapter(bitBucketClient: BitBucketClient,
								   mavenPomParser: MavenPomParser,
								   openShiftPropertyParser: OpenShiftPropertyParser): BitBucketRepositoryAdapter {

		return BitBucketRepositoryAdapter(
			bitBucketClient = bitBucketClient,
			bitBucketAdapterProperties = bitBucketAdapterProperties,
			mavenPomParser = mavenPomParser,
			openShiftPropertyParser = openShiftPropertyParser
		)
	}

	@Bean
	fun bitBucketClient(feignEncoder: Encoder,
						feignDecoder: Decoder,
						bitBucketClientAuthenticationInterceptor: RequestInterceptor): BitBucketClient {

		return Feign.builder()
			.options(Request.Options(bitBucketAdapterProperties.connectTimeoutMillis.toInt(), bitBucketAdapterProperties.readTimeoutMillis.toInt()))
			.encoder(feignEncoder)
			.decoder(feignDecoder)
			.decode404()
			.requestInterceptor(bitBucketClientAuthenticationInterceptor)
			.target(BitBucketClient::class.java, bitBucketAdapterProperties.url!!.toString())
	}

	@Bean
	fun bitBucketClientAuthenticationInterceptor(): RequestInterceptor {

		return RequestInterceptor {
			it.header("Authorization", "Bearer ${bitBucketAdapterProperties.token!!}")
		}
	}
}
