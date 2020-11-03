package de.darkatra.projectoverview.check

import org.springframework.http.HttpStatus
import org.springframework.util.LinkedCaseInsensitiveMap
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class CheckController(
	private val checkService: CheckService
) {

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@PostMapping("/checks")
	fun queueCheck() {

		// TODO: read from request
		val check = Check(
			pluginName = "maven",
			checkType = "parent",
			repositorySlug = "project-overview-backend",
			checkConfiguration = CheckConfiguration(
				name = "spring-boot-parent-check",
				properties = LinkedCaseInsensitiveMap<String>().also {
					it["groupId"] = "org.springframework.boot"
					it["artifactId"] = "spring-boot-starter-parent"
					it["versionResolver"] = "url:https://repo1.maven.org/maven2"
				}
			)
		)

		checkService.queueCheck(check)
	}
}
