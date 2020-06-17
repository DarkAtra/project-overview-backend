package de.idealo.projectoverviewhackday.clients.check

import de.idealo.projectoverviewhackday.clients.check.model.CheckEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckAdapter : MongoRepository<CheckEntity, String>
