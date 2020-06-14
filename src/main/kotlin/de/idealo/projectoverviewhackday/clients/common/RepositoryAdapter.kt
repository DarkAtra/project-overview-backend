package de.idealo.projectoverviewhackday.clients.common

import de.idealo.projectoverviewhackday.model.Repository
import java.nio.file.Path

interface RepositoryAdapter {

	fun getRepositories(project: String): List<Repository>

	// TODO: Path vs File
	fun cloneRepository(repository: Repository, localRepositoryPath: Path)

	// TODO: Path vs File
	fun pullRepository(localRepositoryPath: Path)
}
