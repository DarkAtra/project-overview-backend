package de.idealo.projectoverviewhackday.model

data class Version(
	val major: Long,
	val minor: Long,
	val build: Long
) {
	fun getVersionDifference(other: Version?): Version? {
		return if (other != null) Version(
			major = this.major - other.major,
			minor = this.minor - other.minor,
			build = this.build - other.build
		) else {
			null
		}
	}

	override fun toString(): String {
		return "$major.$minor.$build"
	}
}
