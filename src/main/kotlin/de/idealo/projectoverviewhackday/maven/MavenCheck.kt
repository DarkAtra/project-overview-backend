package de.idealo.projectoverviewhackday.maven

import de.idealo.projectoverviewhackday.base.model.Check
import de.idealo.projectoverviewhackday.base.model.CheckResult
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.Locale

@Component
class MavenCheck(
	private val mavenModelResolver: MavenModelResolver
) : Check {

	companion object {
		const val MODE: String = "mode"
		const val GROUP_ID: String = "groupId"
		const val ARTIFACT_ID: String = "artifactId"
		const val VERSION: String = "version"
	}

	override fun performCheck(directory: Path, parameters: Map<String, String>): CheckResult {

		val model = mavenModelResolver.getModel(directory)

		when (parameters[MODE]?.toLowerCase(Locale.ENGLISH)) {
			"dependency" -> {

				val groupId = parameters[GROUP_ID] ?: error("Parameter '$GROUP_ID' is required for $MODE: 'dependency'")
				val artifactId = parameters[ARTIFACT_ID] ?: error("Parameter '$ARTIFACT_ID' is required for $MODE: 'dependency'")
				val version = parameters[VERSION] ?: error("Parameter '$VERSION' is required for $MODE: 'dependency'")

				val artifact = model.dependencies.firstOrNull { it.groupId == groupId && it.artifactId == artifactId } ?: return CheckResult(
					status = CheckResult.Status.FAILED,
					message = "Dependency '$groupId:$artifactId:$version' not found."
				)

				if (artifact.version != version) {
					return CheckResult(
						status = CheckResult.Status.FAILED,
						message = "Dependency '$groupId:$artifactId' found but version does not match. Wanted version '$version' but was '${artifact.version}'"
					)
				}
			}
			else -> CheckResult(
				status = CheckResult.Status.SKIPPED,
				message = "Invalid check parameters"
			)
		}

		return CheckResult(
			status = CheckResult.Status.SUCCESSFUL,
			message = "Check passed"
		)
	}
}
