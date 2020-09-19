package de.darkatra.projectoverview.maven

import de.darkatra.projectoverview.base.model.Check
import de.darkatra.projectoverview.base.model.CheckName
import de.darkatra.projectoverview.base.model.CheckResult
import de.darkatra.projectoverview.base.model.ForceRefreshCache
import de.darkatra.projectoverview.base.model.Parameter
import de.darkatra.projectoverview.base.model.RepositoryDirectory
import org.springframework.stereotype.Component
import java.nio.file.Path

@Component
@Check("maven")
class MavenCheck(
	private val mavenModelResolver: MavenModelResolver
) {

	companion object {
		const val MODE: String = "mode"
		const val GROUP_ID: String = "groupId"
		const val ARTIFACT_ID: String = "artifactId"
		const val VERSION_RESOLVER: String = "versionResolver"
	}

	fun performCheck(@RepositoryDirectory directory: Path,
	                 @CheckName checkName: String,
	                 @Parameter(MODE) mode: MavenCheckMode,
	                 @Parameter(GROUP_ID) groupId: String,
	                 @Parameter(ARTIFACT_ID) artifactId: String,
	                 @Parameter(VERSION_RESOLVER) versionResolver: VersionResolver,
	                 @ForceRefreshCache forceRefreshCache: Boolean): CheckResult {

		if (forceRefreshCache) {
			mavenModelResolver.evictCache(directory)
		}

		val model = mavenModelResolver.getModel(directory)

		when (mode) {
			MavenCheckMode.DEPENDENCY -> {

				val artifact = model.dependencies.firstOrNull { it.groupId == groupId && it.artifactId == artifactId } ?: return CheckResult(
					checkName = checkName,
					status = CheckResult.Status.FAILED,
					message = "Dependency '$groupId:$artifactId' not found."
				)

				val wantedVersion = versionResolver.resolve(groupId, artifactId)
				if (artifact.version != wantedVersion) {
					return CheckResult(
						checkName = checkName,
						status = CheckResult.Status.FAILED,
						message = "Dependency '$groupId:$artifactId' found but version does not match. Wanted version '$wantedVersion' but was '${artifact.version}'"
					)
				}
			}
			MavenCheckMode.PARENT -> {

				if (model.parent.groupId != groupId && model.parent.artifactId != artifactId) {
					return CheckResult(
						checkName = checkName,
						status = CheckResult.Status.FAILED,
						message = "Parent '$groupId:$artifactId' not found."
					)
				}

				val wantedVersion = versionResolver.resolve(groupId, artifactId)
				if (model.parent.version != wantedVersion) {
					return CheckResult(
						checkName = checkName,
						status = CheckResult.Status.FAILED,
						message = "Parent '$groupId:$artifactId' found but version does not match. Wanted version '$wantedVersion' but was '${model.parent.version}'"
					)
				}
			}
		}

		return CheckResult(
			checkName = checkName,
			status = CheckResult.Status.SUCCESSFUL,
			message = "Check passed"
		)
	}
}
