package de.idealo.projectoverviewhackday.common

import de.idealo.projectoverviewhackday.model.Repository

interface RepositoryAdapter {
	fun getRepositories(project: String): List<Repository>
}
