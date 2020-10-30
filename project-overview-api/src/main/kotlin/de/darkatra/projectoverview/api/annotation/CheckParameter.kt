package de.darkatra.projectoverview.api.annotation


/**
 * @property name the name of the parameter to inject. if not specified, [the parameter name][java.lang.reflect.Parameter.name] will be used
 * @property required whether the parameter is required or not
 */
@MustBeDocumented
@Target(AnnotationTarget.VALUE_PARAMETER)
@Retention(AnnotationRetention.RUNTIME)
annotation class CheckParameter(
	val name: String = "",
	val required: Boolean = true
)
