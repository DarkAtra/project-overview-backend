package de.idealo.projectoverviewhackday.modules.check

import de.idealo.projectoverviewhackday.modules.check.model.CheckDto
import de.idealo.projectoverviewhackday.modules.check.model.PerformCheckRequestDto
import de.idealo.projectoverviewhackday.modules.check.service.CheckService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["\${projectoverview.security.allowed-origins}"])
@RequestMapping("/checks", produces = [MediaType.APPLICATION_JSON_VALUE])
class CheckController(
	private val checkService: CheckService,
	private val checkDtoMapper: CheckDtoMapper
) {

	@GetMapping
	fun getChecks(): List<CheckDto> {

		return checkService.getChecks()
			.map { checkDtoMapper.map(it) }
	}

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	fun performCheck(@Valid @RequestBody performCheckRequest: PerformCheckRequestDto) {

		checkService.performChecks(performCheckRequest.repositoryName)
	}
}
