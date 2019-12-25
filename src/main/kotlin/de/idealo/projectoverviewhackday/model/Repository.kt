package de.idealo.projectoverviewhackday.model

import org.apache.maven.model.Model

data class Repository(
	val name: String,
	val project: String,
	val url: String,
	val parent: Artifact?,
	val dependencies: List<Artifact> = emptyList(),
	val properties: List<Property> = emptyList(),
	val openShiftProperties: List<Property> = emptyList()
) {
	val checkResults: MutableList<CheckResult<out Any>> = mutableListOf()

	data class Builder(
		val name: String,
		val project: String,
		val url: String,
		var parent: Artifact? = null,
		var dependencies: List<Artifact> = emptyList(),
		var properties: List<Property> = emptyList(),
		var openShiftProperties: List<Property> = emptyList(),
		var versionParser: VersionParser = VersionParser()
	) {

		fun model(model: Model?) = apply {
			this.parent = model?.parent?.let { Artifact.of(it, versionParser) }
			this.dependencies = model?.dependencies?.mapNotNull { Artifact.of(it, versionParser) } ?: emptyList()
			this.properties = model?.properties?.mapNotNull { Property(it.key.toString(), it.value.toString()) } ?: emptyList()
		}

		fun openShiftProperties(openShiftProperties: List<Property>) = apply { this.openShiftProperties = openShiftProperties }
		fun build() = Repository(
			name = name,
			project = project,
			url = url,
			parent = parent,
			dependencies = dependencies,
			properties = properties,
			openShiftProperties = openShiftProperties
		)
	}
}
