package de.darkatra.projectoverview.context

import java.lang.reflect.Parameter

interface ArgumentResolver<S : Annotation> {

	fun resolve(parameter: Parameter, checkContext: CheckContext): Any?
}
