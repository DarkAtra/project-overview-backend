package de.darkatra.projectoverview.base.model

import org.eclipse.jgit.api.Git

data class UpsertRepositoryResult(
	val git: Git,
	val hadUpdates: Boolean
)