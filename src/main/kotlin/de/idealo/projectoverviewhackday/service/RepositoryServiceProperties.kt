package de.idealo.projectoverviewhackday.service

import de.idealo.projectoverviewhackday.model.Artifact
import de.idealo.projectoverviewhackday.model.Check
import de.idealo.projectoverviewhackday.model.DependencyCheck
import de.idealo.projectoverviewhackday.model.OpenshiftPropertyCheck
import de.idealo.projectoverviewhackday.model.ParentCheck
import de.idealo.projectoverviewhackday.model.Property
import de.idealo.projectoverviewhackday.model.StaticVersionResolver
import de.idealo.projectoverviewhackday.model.Version
import de.idealo.projectoverviewhackday.model.VersionResolver
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.validation.annotation.Validated
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.PositiveOrZero

@Validated
@ConfigurationProperties("services.repository")
data class RepositoryServiceProperties(

	@field:NotBlank
	var project: String? = null,

	@field:Valid
	@field:NotNull
	var checks: List<CheckTemplate>? = emptyList(),

	@field:Valid
	@field:NotNull
	var checkOverrides: List<CheckOverride>? = emptyList()
) {

	data class CheckTemplate(

		@field:NotNull
		var type: Type? = null,

		@field:NotNull
		@field:Positive
		var id: Long? = null,

		@field:NotBlank
		var name: String? = null,

		@field:NotNull
		var required: Boolean? = true,

		@field:Valid
		var artifact: ArtifactTemplate? = null,

		@field:Valid
		var property: PropertyTemplate? = null
	) {

		enum class Type {
			PARENT,
			DEPENDENCY,
			OPENSHIFT_PROPERTY
		}

		fun build(excludedChecks: List<Long>): Check<out Any>? {

			val required = !excludedChecks.contains(id!!) && required!!
			return when (type) {
				Type.PARENT -> ParentCheck(
					id = id!!,
					name = name!!,
					required = required,
					expectedArtifact = artifact!!.build()
				)
				Type.DEPENDENCY -> DependencyCheck(
					id = id!!,
					name = name!!,
					required = required,
					expectedArtifact = artifact!!.build()
				)
				Type.OPENSHIFT_PROPERTY -> OpenshiftPropertyCheck(
					id = id!!,
					name = name!!,
					required = required,
					expectedProperty = property!!.build()
				)
				else -> throw UnsupportedOperationException("Type '$type' is not supported.")
			}
		}
	}

	data class ArtifactTemplate(

		@field:NotBlank
		var groupId: String? = null,

		@field:NotBlank
		var artifactId: String? = null,

		@field:Valid
		@field:NotNull
		var versionResolver: VersionResolverTemplate? = null
	) {

		fun build(): Artifact {
			return Artifact(
				groupId = groupId!!,
				artifactId = artifactId!!,
				version = versionResolver!!.build().resolve()
			)
		}
	}

	data class PropertyTemplate(

		@field:NotBlank
		var key: String? = null,

		@field:NotBlank
		var value: String? = null
	) {

		fun build(): Property {
			return Property(
				key = key!!,
				value = value!!
			)
		}
	}

	data class VersionResolverTemplate(

		@field:NotNull
		var type: Type? = null,

		@field:Valid
		var version: VersionTemplate? = null
	) {

		enum class Type {
			STATIC
		}

		fun build(): VersionResolver {
			return when (type) {
				Type.STATIC -> StaticVersionResolver(
					version = version!!.build()
				)
				else -> throw UnsupportedOperationException("Type '$type' is not supported.")
			}
		}
	}

	data class VersionTemplate(

		@field:NotNull
		@field:PositiveOrZero
		var major: Long? = null,

		@field:NotNull
		@field:PositiveOrZero
		var minor: Long? = null,

		@field:NotNull
		@field:PositiveOrZero
		var build: Long? = null
	) {

		fun build(): Version {
			return Version(
				major = major!!,
				minor = minor!!,
				build = build!!
			)
		}
	}

	data class CheckOverride(
		@field:NotBlank
		var repository: String? = null,

		@field:NotNull
		var excludedChecks: List<Long>? = emptyList()
	)
}
