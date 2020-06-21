package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckResult
import de.idealo.projectoverviewhackday.base.model.RepositoryDirectory
import org.springframework.core.convert.ConversionException
import org.springframework.core.convert.ConversionService
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.nio.file.Path
import de.idealo.projectoverviewhackday.base.model.Parameter as ParameterAnnotation

@Service
class CheckService(
	private val checkBuilder: CheckBuilder,
	private val checkConfigurationAdapter: CheckConfigurationAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter,
	private val repositoryService: RepositoryService,
	private val conversionService: ConversionService
) : LoggingAware() {

	fun getCheckConfigurations(): List<CheckConfiguration> = checkConfigurationAdapter.findAll()

	fun performChecks(repositoryName: String): List<CheckResult> {

		val checks = checkToRepositoryAdapter.findByIdRepositoryId(repositoryName)
			.mapNotNull { checkConfigurationAdapter.findById(it.id.checkId).orElse(null) }

		val repository = repositoryService.getRepository(repositoryName)
		return repositoryService.upsertAndGetLocalRepository(repository).use { git ->

			val localRepositoryPath = repositoryService.getLocalRepositoryPath(repository)
			checks.mapNotNull { checkConfiguration ->
				val check = checkBuilder.getCheck(
					checkConfiguration = checkConfiguration
				)

				try {
					val performCheckMethod = getPerformCheckMethod(check.javaClass)

					val args = performCheckMethod.parameters.map { parameter ->
						resolveParameterValue(parameter, localRepositoryPath, checkConfiguration.additionalProperties)
					}

					performCheckMethod.invoke(check, *args.toTypedArray()) as CheckResult
				} catch (e: IllegalStateException) {
					log.error("Will not perform check '${checkConfiguration.name}' due to the following reason: ${e.message}", e)
					null
				}
			}
		}
	}

	private fun resolveParameterValue(parameter: Parameter, repositoryDirectory: Path, additionalProperties: Map<String, String>): Any? {

		val parameterAnnotation: ParameterAnnotation? = parameter.getAnnotation(ParameterAnnotation::class.java)
		val repositoryDirectoryAnnotation: RepositoryDirectory? = parameter.getAnnotation(RepositoryDirectory::class.java)

		if (parameterAnnotation != null && repositoryDirectoryAnnotation != null) {
			error("Found multiple check related annotations for Parameter '${parameter.name}'.")
		}

		when {
			parameterAnnotation != null -> {
				val parameterName = if (parameterAnnotation.name == "") parameter.name else parameterAnnotation.name
				val required = parameterAnnotation.required
				val parameterForName = additionalProperties[parameterName]

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
			repositoryDirectoryAnnotation != null -> return repositoryDirectory
			else -> error("Found not check related annotations for Parameter '${parameter.name}'.")
		}
	}

	private fun getPerformCheckMethod(checkClass: Class<Any>): Method {

		val performCheckMethods = ReflectionUtils.getDeclaredMethods(checkClass)
			.filter { method -> method.name == "performCheck" }

		if (performCheckMethods.isEmpty()) {
			error("Found no method with name 'performCheck' in class '$checkClass'.")
		}
		if (performCheckMethods.size > 1) {
			error("Found multiple methods with name 'performCheck' in class '$checkClass'.")
		}

		return performCheckMethods.first()
	}
}
