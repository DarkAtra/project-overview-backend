package de.darkatra.projectoverview.check

data class Check(
	val pluginName: String,
	val checkType: String,
	val repositorySlug: String,
	val checkConfiguration: CheckConfiguration
)
