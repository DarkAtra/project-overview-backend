package de.idealo.projectoverviewhackday.api

import de.idealo.projectoverviewhackday.model.CheckOutcome
import de.idealo.projectoverviewhackday.model.CheckStatus
import de.idealo.projectoverviewhackday.model.Repository
import de.idealo.projectoverviewhackday.service.RepositoryService
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin("*")
@RequestMapping("/repositories", produces = [MediaType.APPLICATION_JSON_VALUE])
class RepositoryController(private val repositoryService: RepositoryService) {

	@GetMapping
	fun getRepositories(@RequestParam(required = false) checkOutcome: CheckOutcome? = null,
						@RequestParam(required = false) checkStatus: CheckStatus? = null): List<Repository> {
		return repositoryService.getRepositories(checkOutcome, checkStatus)
	}

	@Scheduled(fixedDelay = 30000)
	fun refreshCache() {
		repositoryService.evictBitbucketCache()
		arrayOf(*CheckOutcome.values(), null).forEach { checkOutcome ->
			arrayOf(*CheckStatus.values(), null).forEach { checkStatus ->
				repositoryService.reloadCache(checkOutcome, checkStatus)
			}
		}
	}
}
