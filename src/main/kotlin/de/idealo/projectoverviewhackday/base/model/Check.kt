package de.idealo.projectoverviewhackday.base.model

import java.nio.file.Path

interface Check {

	fun performCheck(directory: Path, parameters: Map<String, String>): CheckResult
}
