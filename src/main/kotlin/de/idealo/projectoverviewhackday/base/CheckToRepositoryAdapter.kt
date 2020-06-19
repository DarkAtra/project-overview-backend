package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckToRepository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckToRepositoryAdapter : MongoRepository<CheckToRepository, CheckToRepository.CheckToRepositoryId> {

	fun findByIdRepositoryId(repositoryId: String): List<CheckToRepository>
}
