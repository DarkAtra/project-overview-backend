package de.idealo.projectoverviewhackday.api

import de.idealo.projectoverviewhackday.api.model.CheckConfigurationResponse
import de.idealo.projectoverviewhackday.api.model.CheckResponse
import de.idealo.projectoverviewhackday.api.model.PerformChecksRequest
import de.idealo.projectoverviewhackday.base.CheckService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@CrossOrigin(origins = ["\${projectoverview.security.allowed-origins}"])
@RequestMapping("/checks", produces = [MediaType.APPLICATION_JSON_VALUE])
class CheckController(
	private val checkService: CheckService
) {

	@GetMapping
	fun getCheckConfigurations(): List<CheckConfigurationResponse> {

		return checkService.getCheckConfigurations()
			.map { checkConfiguration ->
				CheckConfigurationResponse(
					name = checkConfiguration.name
				)
			}
	}

	@GetMapping("/{checkName}")
	fun getCheckResults(@PathVariable checkName: String, @RequestParam repositoryName: String): List<CheckResponse> {

		return checkService.getCheckResults(checkName = checkName, repositoryName = repositoryName)
			.map { checkResult ->
				CheckResponse(
					checkName = checkResult.checkName,
					status = CheckResponse.Status.valueOf(checkResult.status.name),
					message = checkResult.message
				)
			}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun performChecks(@Valid @RequestBody performChecksRequest: PerformChecksRequest): List<CheckResponse> {

		return checkService.performChecks(performChecksRequest.repositoryName)
			.map { checkResult ->
				CheckResponse(
					checkName = checkResult.checkName,
					status = CheckResponse.Status.valueOf(checkResult.status.name),
					message = checkResult.message
				)
			}
	}
}
