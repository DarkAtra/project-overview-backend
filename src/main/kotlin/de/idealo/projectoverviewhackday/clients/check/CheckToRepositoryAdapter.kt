package de.idealo.projectoverviewhackday.clients.check

import de.idealo.projectoverviewhackday.clients.check.model.CheckToRepositoryEntity
import de.idealo.projectoverviewhackday.clients.check.model.CheckToRepositoryIdEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckToRepositoryAdapter : MongoRepository<CheckToRepositoryEntity, CheckToRepositoryIdEntity> {

	fun findByIdRepositoryId(repositoryId: String): List<CheckToRepositoryEntity>
}
