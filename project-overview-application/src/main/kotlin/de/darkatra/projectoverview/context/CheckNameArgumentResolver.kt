package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.annotation.CheckName
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class CheckNameArgumentResolver : ArgumentResolver<CheckName> {

	override fun resolve(parameter: Parameter, checkContext: CheckContext): String {
		return checkContext.checkName
	}
}
