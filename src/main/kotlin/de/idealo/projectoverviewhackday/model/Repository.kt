package de.idealo.projectoverviewhackday.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.apache.maven.model.Model

data class Repository(
	val name: String,
	val project: String,
	@JsonIgnore
	val parent: Artifact?,
	@JsonIgnore
	val dependencies: List<Artifact> = emptyList(),
	@JsonIgnore
	val properties: List<Property> = emptyList(),
	@JsonIgnore
	val openShiftProperties: List<Property> = emptyList()
) {
	val checkResults: MutableList<CheckResult> = mutableListOf()

	data class Builder(
		val name: String,
		val project: String,
		var parent: Artifact? = null,
		var dependencies: List<Artifact> = emptyList(),
		var properties: List<Property> = emptyList(),
		var openShiftProperties: List<Property> = emptyList()
	) {

		companion object {
			fun of(name: String, project: String, model: Model?): Builder {
				return Builder(
					name = name,
					project = project,
					parent = model?.parent?.let(Artifact.Companion::of),
					dependencies = model?.dependencies?.mapNotNull(Artifact.Companion::of) ?: emptyList(),
					properties = model?.properties?.mapNotNull { Property(it.key.toString(), it.value.toString()) } ?: emptyList()
				)
			}
		}

		fun parent(parent: Artifact?) = apply { this.parent = parent }
		fun dependencies(dependencies: List<Artifact>) = apply { this.dependencies = dependencies }
		fun properties(properties: List<Property>) = apply { this.properties = properties }
		fun openShiftProperties(openShiftProperties: List<Property>) = apply { this.openShiftProperties = openShiftProperties }
		fun build() = Repository(
			name = name,
			project = project,
			parent = parent,
			dependencies = dependencies,
			properties = properties,
			openShiftProperties = openShiftProperties
		)
	}
}
