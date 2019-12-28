package de.idealo.projectoverviewhackday.clients.common

import org.apache.maven.model.Dependency
import org.apache.maven.model.Model
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class MavenPropertyResolver {
	private val pattern = Pattern.compile("\\\$\\{([a-zA-Z.-]+)}")

	fun resolveVersion(model: Model?): Model? {
		model?.dependencies?.forEach { resolveVersion(model, it) }
		return model
	}

	private fun resolveVersion(model: Model, dependency: Dependency): Dependency {

		dependency.version?.let { version ->
			val matcher = pattern.matcher(version)
			matcher.results().findFirst().ifPresent { matchResult ->
				model.properties.getProperty(matchResult.group(1))?.let { dependency.version = version.replace(matchResult.group(0), it) }
			}
		}
		return dependency
	}
}
