package de.idealo.projectoverviewhackday.modules.repository

import de.idealo.projectoverviewhackday.modules.repository.model.RepositoryDto
import de.idealo.projectoverviewhackday.modules.repository.service.RepositoryService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@RestController
@CrossOrigin(origins = ["\${projectoverview.security.allowed-origins}"])
@RequestMapping("/repositories", produces = [MediaType.APPLICATION_JSON_VALUE])
class RepositoryController(
	private val repositoryService: RepositoryService,
	private val repositoryDtoMapper: RepositoryDtoMapper
) {

	@GetMapping
	fun getRepositories(): List<RepositoryDto> {

		return repositoryService.getRepositories()
			.map { repositoryDtoMapper.map(it) }
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createRepository(@Valid @RequestBody repository: RepositoryDto) {

		repositoryService.createRepository(repositoryDtoMapper.map(repository))
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun deleteRepository(@Valid @NotBlank @RequestParam name: String) {

		repositoryService.deleteRepository(name)
	}
}
