package de.idealo.projectoverviewhackday.api

import de.idealo.projectoverviewhackday.model.Repository
import de.idealo.projectoverviewhackday.service.RepositoryService
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["\${security.allowed-origins}"])
@RequestMapping("/repositories", produces = [MediaType.APPLICATION_JSON_VALUE])
class RepositoryController(private val repositoryService: RepositoryService) {

	// TODO: redesign the api
	@GetMapping
	fun getRepositories(): List<Repository> {

		return emptyList()
	}
}
