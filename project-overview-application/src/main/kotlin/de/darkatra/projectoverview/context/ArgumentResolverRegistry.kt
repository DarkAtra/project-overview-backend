package de.darkatra.projectoverview.context

import org.springframework.core.ResolvableType
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class ArgumentResolverRegistry(
	argumentResolvers: List<ArgumentResolver<*>>
) {

	private val typeToArgumentResolverMap = mutableMapOf<ResolvableType, ArgumentResolver<*>>()

	init {
		argumentResolvers.forEach { argumentResolver ->
			val type = getTypeFromGeneric(argumentResolver)
				?: throw IllegalArgumentException(
					"Unable to determine source type <S> for ArgumentResolver '${argumentResolver.javaClass.name}'. Does the ArgumentResolver parameterize the type?"
				)

			typeToArgumentResolverMap[type] = argumentResolver
		}
	}

	fun resolve(parameter: Parameter, checkContext: CheckContext): Any? {

		val parameterResolvers = parameter.annotations.mapNotNull { getParameterValueResolver(it) }

		when {
			parameterResolvers.size > 1 -> error("Found multiple ArgumentResolver for Parameter '${parameter.name}'.")
			parameterResolvers.isEmpty() -> error("Found no ArgumentResolver for Parameter '${parameter.name}'.")
		}

		return parameterResolvers.first().resolve(parameter, checkContext)
	}

	// TODO: what should happen when multiple ArgumentResolvers exist for the same type
	private fun getParameterValueResolver(annotation: Annotation): ArgumentResolver<*>? {
		return typeToArgumentResolverMap
			.filter { (resolvableType, _) -> resolvableType.toClass().isAssignableFrom(annotation.javaClass) }
			.values.firstOrNull()
	}

	private fun getTypeFromGeneric(argumentResolver: ArgumentResolver<*>): ResolvableType? {
		val resolvableType = ResolvableType.forClass(argumentResolver.javaClass).`as`(ArgumentResolver::class.java)
		val generic = resolvableType.generics[0]
		return when {
			generic.resolve() == null -> null
			else -> generic
		}
	}
}
