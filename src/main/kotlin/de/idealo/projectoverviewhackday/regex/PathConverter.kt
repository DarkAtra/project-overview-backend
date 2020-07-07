package de.idealo.projectoverviewhackday.regex

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
class PathConverter : Converter<String, Path> {
	override fun convert(source: String): Path {
		return Path.of(source)
	}
}
