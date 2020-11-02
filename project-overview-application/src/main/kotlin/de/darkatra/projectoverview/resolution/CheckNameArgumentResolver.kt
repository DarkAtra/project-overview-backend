package de.darkatra.projectoverview.resolution

import de.darkatra.projectoverview.api.annotation.CheckName
import de.darkatra.projectoverview.context.CheckContext
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter

@Component
class CheckNameArgumentResolver : ArgumentResolver<CheckName> {

	override fun resolve(parameter: Parameter, checkContext: CheckContext): String {
		return checkContext.getCheckName()
	}
}
