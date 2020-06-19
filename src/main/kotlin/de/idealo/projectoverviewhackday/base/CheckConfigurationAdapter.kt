package de.idealo.projectoverviewhackday.base

import de.idealo.projectoverviewhackday.base.model.CheckConfiguration
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckConfigurationAdapter : MongoRepository<CheckConfiguration, String>
