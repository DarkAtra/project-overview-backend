package de.darkatra.projectoverview.resolution

import de.darkatra.projectoverview.context.CheckContext
import java.lang.reflect.Parameter

interface ArgumentResolver<S : Annotation> {

	fun resolve(parameter: Parameter, checkContext: CheckContext): Any?
}
