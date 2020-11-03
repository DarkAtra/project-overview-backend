package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.annotation.Check
import de.darkatra.projectoverview.api.check.CheckOutcome
import de.darkatra.projectoverview.api.check.CheckResult
import de.darkatra.projectoverview.resolution.ArgumentResolverRegistry
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.time.Instant

class InvokablePluginTarget(
	private val plugin: Plugin,
	private val argumentResolverRegistry: ArgumentResolverRegistry
) {

	// TODO: perform these checks when the plugin is loaded
	private val methodsWithCheckAnnotation: List<Method> = plugin.bean::class.java.methods
		.filter { method -> method.modifiers and Modifier.PUBLIC != 0 }
		.filter { method -> method.isAnnotationPresent(Check::class.java) }
		.filter { method -> method.hasValidReturnType() }
	// TODO: add further signature validations

	fun performCheck(checkType: String, checkContext: CheckContext): CheckResult {

		val methodsForCheckType = methodsWithCheckAnnotation.filter { method -> method.getAnnotation(Check::class.java).type == checkType }

		when {
			methodsForCheckType.isEmpty() -> error("Found no method for checkType '$checkType' in class '${plugin.bean::class.qualifiedName}'.")
			methodsForCheckType.size > 1 -> error("Found multiple methods for checkType '$checkType' in class '${plugin.bean::class.qualifiedName}'.")
		}

		val checkMethod = methodsForCheckType.first()
		val arguments = checkMethod.parameters.map { parameter ->
			argumentResolverRegistry.resolve(parameter, PluginAwareCheckContext(
				plugin = plugin,
				checkName = checkContext.getCheckName(),
				repositoryDirectory = checkContext.getRepositoryDirectory(),
				parameters = checkContext.getParameters()
			))
		}

		return getCheckResult(checkMethod.invoke(plugin.bean, *arguments.toTypedArray()))
	}

	private fun getCheckResult(invocationResult: Any): CheckResult {

		return when (invocationResult) {
			is CheckOutcome -> CheckResult(
				checkOutcome = invocationResult,
				message = "Check succeeded",
				checked = Instant.now()
			)
			is CheckResult -> invocationResult
			else -> throw IllegalStateException("Check invocation returned with an invalid type")
		}
	}
}

private fun Method.hasValidReturnType(): Boolean {
	val returnType = this.returnType
	return CheckOutcome::class.java.isAssignableFrom(returnType)
		|| CheckResult::class.java.isAssignableFrom(returnType)
}
