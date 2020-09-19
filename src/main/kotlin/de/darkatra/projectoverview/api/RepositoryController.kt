package de.darkatra.projectoverview.api

import de.darkatra.projectoverview.api.model.RepositoryCreateRequest
import de.darkatra.projectoverview.api.model.RepositoryDeleteRequest
import de.darkatra.projectoverview.api.model.RepositoryResponse
import de.darkatra.projectoverview.base.RepositoryService
import de.darkatra.projectoverview.base.model.Repository
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["\${projectoverview.security.allowed-origins}"])
@RequestMapping("/repositories", produces = [MediaType.APPLICATION_JSON_VALUE])
class RepositoryController(
	private val repositoryService: RepositoryService
) {

	@GetMapping
	fun getRepositories(): List<RepositoryResponse> {

		return repositoryService.getRepositories()
			.map { repository ->
				RepositoryResponse(
					name = repository.name,
					browseUrl = repository.browseUrl,
					cloneUrl = repository.cloneUrl,
					checks = repository.checks
				)
			}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createRepository(@Valid @RequestBody repositoryCreateRequest: RepositoryCreateRequest): RepositoryResponse {

		val savedRepository = repositoryService.createRepository(
			Repository(
				name = repositoryCreateRequest.name,
				browseUrl = repositoryCreateRequest.browseUrl,
				cloneUrl = repositoryCreateRequest.cloneUrl,
				checks = emptyList()
			)
		)

		return RepositoryResponse(
			name = savedRepository.name,
			browseUrl = savedRepository.browseUrl,
			cloneUrl = savedRepository.cloneUrl,
			checks = savedRepository.checks
		)
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun deleteRepository(@Valid @RequestBody repositoryDeleteRequest: RepositoryDeleteRequest) {

		repositoryService.deleteRepository(repositoryDeleteRequest.name)
	}
}
