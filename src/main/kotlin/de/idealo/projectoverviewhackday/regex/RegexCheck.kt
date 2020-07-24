package de.idealo.projectoverviewhackday.regex

import de.idealo.projectoverviewhackday.base.model.Check
import de.idealo.projectoverviewhackday.base.model.CheckName
import de.idealo.projectoverviewhackday.base.model.CheckResult
import de.idealo.projectoverviewhackday.base.model.Parameter
import de.idealo.projectoverviewhackday.base.model.RepositoryDirectory
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.regex.Pattern

@Component
@Check("regex")
class RegexCheck {

	companion object {
		const val MODE: String = "mode"
		const val FILE: String = "file"
		const val PATTERN: String = "pattern"
	}

	fun performCheck(@RepositoryDirectory directory: Path,
	                 @CheckName checkName: String,
	                 @Parameter(MODE) mode: RegexCheckMode,
	                 @Parameter(FILE) file: Path,
	                 @Parameter(PATTERN) pattern: Pattern): CheckResult {

		val target = directory.resolve(file).toRealPath()
		val fileIsInRepositoryDirectory = target.startsWith(directory.toRealPath())
		if (!fileIsInRepositoryDirectory) {
			return CheckResult(
				checkName = checkName,
				status = CheckResult.Status.FAILED,
				message = "File '$file' is not inside of folder '$directory'."
			)
		}

		val fileContent = target.toFile().readBytes().toString(StandardCharsets.UTF_8)
		val matcher = pattern.matcher(fileContent)

		if (!matcher.matches()) {
			return CheckResult(
				checkName = checkName,
				status = CheckResult.Status.FAILED,
				message = "Regex did not match."
			)
		}

		return CheckResult(
			checkName = checkName,
			status = CheckResult.Status.SUCCESSFUL,
			message = "Check passed"
		)
	}
}
