package de.idealo.projectoverviewhackday.clients.model

import feign.Param
import java.util.Locale

enum class OpenShiftPropertyTarget() {
	TESTING,
	STAGING,
	SANDBOX,
	PRODUCTION,
	PROJECT;

	class Expander : Param.Expander {
		override fun expand(value: Any?): String {
			return if (value is OpenShiftPropertyTarget) {
				value.name.toLowerCase(Locale.ENGLISH)
			} else {
				Param.ToStringExpander().expand(value)
			}
		}
	}
}

