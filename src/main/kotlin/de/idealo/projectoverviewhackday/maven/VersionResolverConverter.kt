package de.idealo.projectoverviewhackday.maven

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.net.URL

@Component
class VersionResolverConverter : Converter<String, VersionResolver> {
	override fun convert(source: String): VersionResolver {
		return when {
			source.startsWith("url:") || source.startsWith("true:url:") || source.startsWith("false:url:") -> {
				MavenUrlVersionResolver(
					url = URL(source.substringAfter("url:")),
					compareToLatest = source.substringBefore(delimiter = "url:") == "true:"
				)
			}
			source.startsWith("static:") -> {
				StaticVersionResolver(
					version = source.substringAfter("static:")
				)
			}
			else -> throw IllegalArgumentException("Could not convert from String '$source' to VersionResolver.")
		}
	}
}
