package de.darkatra.projectoverview.base

import de.darkatra.projectoverview.base.model.CheckConfiguration
import java.lang.reflect.Parameter
import java.nio.file.Path

interface ParameterValueResolver<A : Annotation> {

	// TODO: introduce a new exception for errors during parameter value resolution?
	fun resolve(parameter: Parameter, checkConfiguration: CheckConfiguration, localRepositoryPath: Path, forceRefreshCache: Boolean): Any?
}