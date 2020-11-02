package de.darkatra.projectoverview.resolution

import de.darkatra.projectoverview.api.annotation.CheckParameter
import de.darkatra.projectoverview.context.CheckContext
import de.darkatra.projectoverview.context.Plugin
import de.darkatra.projectoverview.context.PluginAwareCheckContext
import org.springframework.core.convert.ConversionException
import org.springframework.core.convert.ConversionService
import org.springframework.core.convert.converter.Converter
import org.springframework.core.convert.support.GenericConversionService
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class CheckParameterArgumentResolver(
	private val conversionService: ConversionService,
) : ArgumentResolver<CheckParameter> {

	override fun resolve(parameter: Parameter, checkContext: CheckContext): Any? {

		val checkParameterAnnotation = parameter.getAnnotation(CheckParameter::class.java)

		val parameterName = if (checkParameterAnnotation.name == "") parameter.name else checkParameterAnnotation.name
		val isRequired = checkParameterAnnotation.required

		val parameterValue = checkContext.getParameters()[parameterName]
		if (parameterValue == null && isRequired) {
			error("No value found for required CheckParameter '${parameter.name}'.")
		}

		if (parameterValue == null) {
			return null
		}

		if (parameterValue.javaClass.isAssignableFrom(parameter.type)) {
			return parameterValue
		}

		if (checkContext is PluginAwareCheckContext) {
			val pluginConversionService = getPluginConversionService(checkContext.getPlugin())
			try {
				return pluginConversionService.convert(parameterValue, parameter.type)
			} catch (e: ConversionException) {
				// just ignore it and try the default conversionService
			}
		}

		return conversionService.convert(parameterValue, parameter.type)
	}

	private fun getPluginConversionService(plugin: Plugin): ConversionService {
		return GenericConversionService().also {
			plugin.applicationContext.getBeansOfType(Converter::class.java).values
				.forEach(it::addConverter)
		}
	}
}
