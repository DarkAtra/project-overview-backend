package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckResult
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method

@Service
class CheckService(
	private val checkBuilder: CheckBuilder,
	private val checkConfigurationAdapter: CheckConfigurationAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter,
	private val repositoryService: RepositoryService,
	private val parameterValueResolverRegistry: ParameterValueResolverRegistry
) : LoggingAware() {

	fun getCheckConfigurations(): List<CheckConfiguration> = checkConfigurationAdapter.findAll()

	fun performChecks(repositoryName: String): List<CheckResult> {

		val checks = checkToRepositoryAdapter.findByIdRepositoryId(repositoryName)
			.mapNotNull { checkConfigurationAdapter.findById(it.id.checkId).orElse(null) }

		val repository = repositoryService.getRepository(repositoryName)
		return repositoryService.upsertAndGetLocalRepository(repository).use { _ ->

			val localRepositoryPath = repositoryService.getLocalRepositoryPath(repository)
			checks.map { checkConfiguration ->
				val check = checkBuilder.getCheck(
					checkConfiguration = checkConfiguration
				)

				val (performCheckMethod, args) = try {
					val performCheckMethod = getPerformCheckMethod(check.javaClass)

					val args = performCheckMethod.parameters.map { parameter ->
						parameterValueResolverRegistry.resolve(parameter, checkConfiguration, localRepositoryPath)
					}

					Pair(performCheckMethod, args)
				} catch (e: IllegalStateException) {
					log.error("Will not perform check '${checkConfiguration.name}' due to the following reason: ${e.message}", e)
					return@map CheckResult(
						checkName = checkConfiguration.name,
						status = CheckResult.Status.SKIPPED,
						message = "This check was skipped."
					)
				}

				try {
					performCheckMethod.invoke(check, *args.toTypedArray()) as CheckResult
				} catch (e: Exception) {
					log.error("Exception performing check '${checkConfiguration.name}':", e)
					return@map CheckResult(
						checkName = checkConfiguration.name,
						status = CheckResult.Status.ABORTED,
						message = "This check was aborted. This is most likely due to an exception."
					)
				}.also { checkResult ->
					checkResult.checkName = checkConfiguration.name
				}
			}
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

		val performCheckMethod = performCheckMethods.first()
		if (CheckResult::class.equals(performCheckMethod.returnType)) {
			error("Found a 'performCheck' method with a wrong return type in class '$checkClass'.")
		}

		return performCheckMethod
	}
}