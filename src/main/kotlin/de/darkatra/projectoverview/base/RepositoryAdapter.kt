package de.darkatra.projectoverview.base

import de.darkatra.projectoverview.base.model.Repository
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository as RepositoryMarker

@RepositoryMarker
interface RepositoryAdapter : MongoRepository<Repository, String>
