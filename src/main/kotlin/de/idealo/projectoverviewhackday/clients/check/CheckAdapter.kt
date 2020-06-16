package de.idealo.projectoverviewhackday.clients.check

import de.idealo.projectoverviewhackday.clients.check.model.CheckEntity
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface CheckAdapter : PagingAndSortingRepository<CheckEntity, String>
