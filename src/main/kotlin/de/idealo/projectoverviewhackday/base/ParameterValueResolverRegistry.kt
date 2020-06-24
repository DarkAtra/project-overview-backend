package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import org.springframework.core.ResolvableType
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter
import java.nio.file.Path

@Component
class ParameterValueResolverRegistry(
	parameterValueResolvers: List<ParameterValueResolver<*>>
) {

	private val annotationParameterValueResolverMap = mutableMapOf<ResolvableType, ParameterValueResolver<*>>()

	init {
		parameterValueResolvers.forEach { parameterValueResolver ->
			val requiredTypeInfo = getRequiredTypeInfo(parameterValueResolver)
				?: throw IllegalArgumentException(
					"Unable to determine source type <A> for your ParameterValueResolver '${parameterValueResolver.javaClass.name}' does the class parameterize the type?"
				)

			annotationParameterValueResolverMap.put(requiredTypeInfo[0], parameterValueResolver)
		}
	}

	fun resolve(parameter: Parameter, checkConfiguration: CheckConfiguration, localRepositoryPath: Path): Any? {

		val parameterResolvers = parameter.annotations.mapNotNull { getParameterValueResolver(it) }

		when {
			parameterResolvers.size > 1 -> error("Found multiple ParameterValueResolvers for Parameter '${parameter.name}'.")
			parameterResolvers.isEmpty() -> error("Found no ParameterValueResolvers for Parameter '${parameter.name}'.")
		}

		return parameterResolvers.first().resolve(parameter, checkConfiguration, localRepositoryPath)
	}

	// TODO: what should happen when multiple ParameterValueResolvers exist for the same annotation
	private fun getParameterValueResolver(annotation: Annotation): ParameterValueResolver<*>? {
		return annotationParameterValueResolverMap
			.filter { (resolvableType, _) -> (resolvableType.type as Class<*>).isAssignableFrom(annotation.javaClass) }
			.values.firstOrNull()
	}

	private fun getRequiredTypeInfo(parameterValueResolver: ParameterValueResolver<*>): Array<ResolvableType>? {
		val resolvableType = ResolvableType.forClass(parameterValueResolver.javaClass).`as`(ParameterValueResolver::class.java)
		val generics = resolvableType.generics
		if (generics.isEmpty()) {
			return null
		}
		generics[0].resolve() ?: return null
		return generics
	}
}