package de.darkatra.projectoverview.context

import org.springframework.core.ResolvableType
import org.springframework.core.annotation.AnnotationAwareOrderComparator
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class ArgumentResolverRegistry(
	argumentResolvers: List<ArgumentResolver<*>>
) {

	private val typeToArgumentResolversMap = argumentResolvers.groupBy { argumentResolver ->
		getTypeFromGeneric(argumentResolver)
			?: throw IllegalArgumentException(
				"Unable to determine source type <S> for ArgumentResolver '${argumentResolver.javaClass.name}'. Does the ArgumentResolver parameterize the type?"
			)
	}

	fun resolve(parameter: Parameter, checkContext: CheckContext): Any? {

		val parameterResolver = getArgumentResolverForParameter(parameter)
			?: error("Found no ArgumentResolver for Parameter '${parameter.name}'.")

		return try {
			parameterResolver.resolve(parameter, checkContext)
		} catch (e: Exception) {
			throw ArgumentResolutionException(
				"An exception occurred while resolving the value for parameter '${parameter.name}' of method '${parameter.declaringExecutable.name}'.", e
			)
		}
	}

	// TODO: is it required to have the order comparator or should it just throw an exception if multiple ArgumentResolvers where found
	/**
	 * Finds all applicable [ArgumentResolvers][ArgumentResolver] for the given [Parameter],
	 * sorts them using Spring's [AnnotationAwareOrderComparator] and returns the first match or `null` if no [ArgumentResolver] was applicable.
	 */
	private fun getArgumentResolverForParameter(parameter: Parameter): ArgumentResolver<*>? {

		return typeToArgumentResolversMap
			.filterKeys { resolvableType ->
				parameter.annotations.any { annotation -> resolvableType.toClass().isAssignableFrom(annotation.javaClass) }
			}
			.flatMap { it.value }
			.sortedWith(AnnotationAwareOrderComparator.INSTANCE)
			.firstOrNull()
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
