package de.darkatra.projectoverview.base

import de.darkatra.projectoverview.base.model.CheckConfiguration
import de.darkatra.projectoverview.base.model.CheckName
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter
import java.nio.file.Path

@Component
class CheckNameParameterValueResolver : ParameterValueResolver<CheckName> {

	override fun resolve(parameter: Parameter, checkConfiguration: CheckConfiguration, localRepositoryPath: Path, forceRefreshCache: Boolean): Any {
		return checkConfiguration.name
	}
}