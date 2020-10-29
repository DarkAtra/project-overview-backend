package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.annotation.CheckParameter
import org.springframework.core.convert.ConversionException
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class CheckParameterArgumentResolver(
	private val conversionService: ConversionService
) : ArgumentResolver<CheckParameter> {

	override fun resolve(parameter: Parameter, checkContext: CheckContext): Any? {

		val checkParameterAnnotation = parameter.getAnnotation(CheckParameter::class.java)

		val parameterName = if (checkParameterAnnotation.name == "") parameter.name else checkParameterAnnotation.name
		val isRequired = checkParameterAnnotation.required

		// TODO: currently is case sensitive. probably not a good idea
		val parameterValue = checkContext.parameters[parameterName]
		if (parameterValue == null && isRequired) {
			error("No value found for required CheckParameter '${parameter.name}'.")
		}

		if (parameterValue != null && parameterValue.javaClass.isAssignableFrom(parameter.type)) {
			return parameterValue
		}

		try {
			return conversionService.convert(parameterValue, parameter.type)
		} catch (e: ConversionException) {
			throw IllegalStateException("Failed to convert value '$parameterValue' for Parameter '${parameter.name}' from 'String' to '${parameter.type}'.", e)
		}
	}
}
