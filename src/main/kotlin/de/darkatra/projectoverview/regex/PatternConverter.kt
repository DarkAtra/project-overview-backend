package de.darkatra.projectoverview.regex

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class PatternConverter : Converter<String, Pattern> {
	override fun convert(source: String): Pattern {
		return Pattern.compile(source)
	}
}
