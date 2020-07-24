package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckResult
import de.idealo.projectoverviewhackday.base.model.CheckToRepository
import de.idealo.projectoverviewhackday.base.model.Repository
import org.springframework.stereotype.Service
import org.springframework.util.ReflectionUtils
import java.lang.reflect.Method
import java.nio.file.Path
import java.time.Instant

@Service
class CheckService(
	private val checkBuilder: CheckBuilder,
	private val checkConfigurationAdapter: CheckConfigurationAdapter,
	private val checkToRepositoryAdapter: CheckToRepositoryAdapter,
	private val repositoryService: RepositoryService,
	private val parameterValueResolverRegistry: ParameterValueResolverRegistry
) : LoggingAware() {

	fun getCheckConfigurations(): List<CheckConfiguration> = checkConfigurationAdapter.findAll()

	fun getCheckResults(checkName: String, repositoryName: String): List<CheckResult> {

		return checkToRepositoryAdapter.findById(CheckToRepository.CheckToRepositoryId(
			checkId = checkName,
			repositoryId = repositoryName
		)).map<List<CheckResult>> { it.checkResults }.orElse(emptyList())
	}

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

				getCheckResult(checkConfiguration, check, localRepositoryPath)
					.let { checkResult ->
						when (checkResult.checkName == checkConfiguration.name) {
							true -> checkResult
							false -> CheckResult(
								checkName = checkConfiguration.name,
								status = CheckResult.Status.ABORTED,
								message = "CheckResult's checkName does not match the checkConfiguration's name. This check was aborted."
							)
						}
					}
					.also { checkResult ->
						persistCheckResult(repository, checkConfiguration, checkResult)
					}
			}
		}
	}

	private fun getCheckResult(checkConfiguration: CheckConfiguration, check: Any, localRepositoryPath: Path): CheckResult {

		val (performCheckMethod, args) = try {

			val performCheckMethod = getPerformCheckMethod(check.javaClass)
			val args = performCheckMethod.parameters.map { parameter ->
				parameterValueResolverRegistry.resolve(parameter, checkConfiguration, localRepositoryPath)
			}

			Pair(performCheckMethod, args)
		} catch (e: IllegalStateException) {
			log.error("Will not perform check '${checkConfiguration.name}' due to the following reason: ${e.message}", e)
			return CheckResult(
				checkName = checkConfiguration.name,
				status = CheckResult.Status.SKIPPED,
				message = "This check was skipped."
			)
		}

		return try {
			performCheckMethod.invoke(check, *args.toTypedArray()) as CheckResult
		} catch (e: Exception) {
			log.error("Exception performing check '${checkConfiguration.name}'.", e)
			CheckResult(
				checkName = checkConfiguration.name,
				status = CheckResult.Status.ABORTED,
				message = "This check was aborted. This is most likely due to an exception."
			)
		}
	}

	private fun persistCheckResult(repository: Repository, checkConfiguration: CheckConfiguration, checkResult: CheckResult) {

		checkToRepositoryAdapter.findById(CheckToRepository.CheckToRepositoryId(
			checkId = checkConfiguration.name,
			repositoryId = repository.name
		)).ifPresent { checkToRepository ->
			checkToRepository.checkResults.add(checkResult.also { it.createdDate = Instant.now() })
			checkToRepositoryAdapter.save(checkToRepository)
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
		if (CheckResult::class == performCheckMethod.returnType) {
			error("Found a 'performCheck' method with a wrong return type in class '$checkClass'.")
		}

		return performCheckMethod
	}
}
