package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.Repository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository as RepositoryMarker

@RepositoryMarker
interface RepositoryAdapter : MongoRepository<Repository, String>
