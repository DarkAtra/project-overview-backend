package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.Check
import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import de.idealo.projectoverviewhackday.base.model.CheckType
import de.idealo.projectoverviewhackday.maven.MavenCheck
import org.springframework.stereotype.Component

@Component
class CheckBuilder(
	private val mavenCheck: MavenCheck
) {

	fun getCheck(checkConfiguration: CheckConfiguration): Check {
		return when (checkConfiguration.checkType) {
			CheckType.MAVEN -> mavenCheck
		}
	}
}
