package de.idealo.projectoverviewhackday.clients.repository

import de.idealo.projectoverviewhackday.clients.repository.model.RepositoryEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface RepositoryAdapter : PagingAndSortingRepository<RepositoryEntity, String> {

	fun getRepositories(): List<RepositoryEntity>
}
