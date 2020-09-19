package de.darkatra.projectoverview.base

import de.darkatra.projectoverview.base.model.Check
import de.darkatra.projectoverview.base.model.CheckConfiguration
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component

@Component
class CheckBuilder(
	private val applicationContext: ApplicationContext
) : LoggingAware() {

	fun getCheck(checkConfiguration: CheckConfiguration): Any {

		val checksForType = applicationContext.getBeansWithAnnotation(Check::class.java)
			.filter { (beanName, _) ->
				applicationContext.findAnnotationOnBean(beanName, Check::class.java)?.type == checkConfiguration.type
			}

		val firstCheck = checksForType.entries.first()

		if (checksForType.size > 1) {
			log.warn("Multiple Beans were found for check type '${checkConfiguration.type}', names: '${checksForType.keys}'. Using '${firstCheck.key}'")
		}

		return firstCheck.value
	}
}
