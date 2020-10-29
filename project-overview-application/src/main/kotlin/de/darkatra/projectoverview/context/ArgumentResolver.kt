package de.darkatra.projectoverview.context

import java.lang.reflect.Parameter

interface ArgumentResolver<S : Annotation> {

	// TODO: introduce a new exception for errors during parameter value resolution
	fun resolve(parameter: Parameter, checkContext: CheckContext): Any?
}
