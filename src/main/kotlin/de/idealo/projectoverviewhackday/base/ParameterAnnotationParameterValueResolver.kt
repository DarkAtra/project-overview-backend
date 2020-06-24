package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import org.springframework.core.convert.ConversionException
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter
import java.nio.file.Path
import de.idealo.projectoverviewhackday.base.model.Parameter as ParameterAnnotation

@Component
class ParameterAnnotationParameterValueResolver(
	private val conversionService: ConversionService
) : ParameterValueResolver<ParameterAnnotation> {

	override fun resolve(parameter: Parameter, checkConfiguration: CheckConfiguration, localRepositoryPath: Path): Any? {

		val parameterAnnotation = parameter.getAnnotation(ParameterAnnotation::class.java)

		val parameterName = if (parameterAnnotation.name == "") parameter.name else parameterAnnotation.name
		val required = parameterAnnotation.required
		val parameterForName = checkConfiguration.additionalProperties[parameterName]

		if (parameterForName == null && required) {
			error("Found no value for required Parameter '${parameter.name}'.")
		}

		if (parameterForName != null && parameterForName.javaClass.isAssignableFrom(parameter.type)) {
			return parameterForName
		}

		try {
			return conversionService.convert(parameterForName, parameter.type)
		} catch (e: ConversionException) {
			throw IllegalStateException("Could not convert value for Parameter '${parameter.name}' from 'String' to '${parameter.type}'.", e)
		}
	}
}