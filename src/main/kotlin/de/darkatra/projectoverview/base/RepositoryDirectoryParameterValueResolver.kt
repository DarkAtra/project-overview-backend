package de.darkatra.projectoverview.base

import de.darkatra.projectoverview.base.model.CheckConfiguration
import de.darkatra.projectoverview.base.model.RepositoryDirectory
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter
import java.nio.file.Path

@Component
class RepositoryDirectoryParameterValueResolver : ParameterValueResolver<RepositoryDirectory> {

	override fun resolve(parameter: Parameter, checkConfiguration: CheckConfiguration, localRepositoryPath: Path): Any {
		return localRepositoryPath
	}
}