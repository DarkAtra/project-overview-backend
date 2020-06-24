package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import java.lang.reflect.Parameter
import java.nio.file.Path

interface ParameterValueResolver<A : Annotation> {

	// TODO: introduce a new exception for errors during parameter value resolution?
	fun resolve(parameter: Parameter, checkConfiguration: CheckConfiguration, localRepositoryPath: Path): Any?
}