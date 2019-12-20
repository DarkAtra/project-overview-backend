package de.idealo.projectoverviewhackday.api

import de.idealo.projectoverviewhackday.model.Repository
import de.idealo.projectoverviewhackday.service.RepositoryService
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/repositories", produces = [MediaType.APPLICATION_JSON_VALUE])
class RepositoryController(private val repositoryService: RepositoryService) {

	@GetMapping
	@Cacheable("repositories")
	fun getRepositories(): List<Repository> {
		return repositoryService.getRepositories()
	}

	@Scheduled(fixedDelay = 30000)
	@CacheEvict("repositories")
	fun evictCache() {
	}
}
