package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.annotation.Check
import de.darkatra.projectoverview.api.check.CheckOutcome
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import de.darkatra.projectoverview.api.annotation.Plugin as PluginAnnotation

class InvokablePluginTarget(
	plugin: Plugin,
	private val argumentResolverRegistry: ArgumentResolverRegistry,
	private val target: Any = plugin.applicationContext.getBean(plugin.applicationContext.getBeanNamesForAnnotation(PluginAnnotation::class.java).first())
) {

	private val methodsWithCheckAnnotation: List<Method>

	init {
		// TODO: perform these checks when the plugin is loaded
		methodsWithCheckAnnotation = target::class.java.methods
			.filter { method -> method.modifiers and Modifier.PUBLIC != 0 }
			.filter { method -> method.isAnnotationPresent(Check::class.java) }
			.filter { method -> method.hasValidReturnType() }
		// TODO: add further signature validations
	}

	fun performCheck(checkType: String, checkContext: CheckContext): Any? {

		val methodsForCheckType = methodsWithCheckAnnotation.filter { method -> method.getAnnotation(Check::class.java).type == checkType }

		when {
			methodsForCheckType.isEmpty() -> error("Found no method for checkType '$checkType' in class '${target::class.qualifiedName}'.")
			methodsForCheckType.size > 1 -> error("Found multiple methods for checkType '$checkType' in class '${target::class.qualifiedName}'.")
		}

		val checkMethod = methodsForCheckType.first()
		val arguments = checkMethod.parameters.map { parameter -> argumentResolverRegistry.resolve(parameter, checkContext) }

		return checkMethod.invoke(target, *arguments.toTypedArray())
	}
}

private fun Method.hasValidReturnType(): Boolean {

	val returnType = this.returnType

	return Void::class.javaPrimitiveType!!.isAssignableFrom(returnType)
		|| CheckOutcome::class.java.isAssignableFrom(returnType)
}
