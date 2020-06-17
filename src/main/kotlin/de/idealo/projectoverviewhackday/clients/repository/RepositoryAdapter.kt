package de.idealo.projectoverviewhackday.clients.repository

import de.idealo.projectoverviewhackday.clients.repository.model.RepositoryEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface RepositoryAdapter : MongoRepository<RepositoryEntity, String>
