package de.idealo.projectoverviewhackday.clients.check

import de.idealo.projectoverviewhackday.clients.check.model.CheckToRepositoryEntity
import de.idealo.projectoverviewhackday.clients.check.model.CheckToRepositoryIdEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckToRepositoryAdapter : PagingAndSortingRepository<CheckToRepositoryEntity, CheckToRepositoryIdEntity> {

	fun findByIdRepositoryId(repositoryId: String): List<CheckToRepositoryEntity>
}
