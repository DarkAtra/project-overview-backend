package de.idealo.projectoverviewhackday.maven

import de.idealo.projectoverviewhackday.base.model.Check
import de.idealo.projectoverviewhackday.base.model.CheckResult
import de.idealo.projectoverviewhackday.base.model.Parameter
import de.idealo.projectoverviewhackday.base.model.RepositoryDirectory
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.util.Locale

@Component
@Check("maven")
class MavenCheck(
	private val mavenModelResolver: MavenModelResolver
) {

	companion object {
		const val MODE: String = "mode"
		const val GROUP_ID: String = "groupId"
		const val ARTIFACT_ID: String = "artifactId"
		const val VERSION: String = "version"
	}

	fun performCheck(@RepositoryDirectory directory: Path, @Parameter(MODE) mode: String, @Parameter groupId: String, @Parameter artifactId: String,
	                 @Parameter version: String): CheckResult {

		val model = mavenModelResolver.getModel(directory)

		when (mode.toLowerCase(Locale.ENGLISH)) {
			"dependency" -> {

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
