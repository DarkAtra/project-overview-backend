package de.idealo.projectoverviewhackday.maven

import org.springframework.core.convert.converter.Converter
import org.springframework.stereotype.Component
import java.util.Locale

@Component
class MavenCheckModeConverter : Converter<String, MavenCheckMode> {
	override fun convert(source: String): MavenCheckMode {
		return MavenCheckMode.valueOf(source.toUpperCase(Locale.ENGLISH))
	}
}
