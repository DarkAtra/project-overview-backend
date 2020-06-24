package de.idealo.projectoverviewhackday.api

import de.idealo.projectoverviewhackday.api.model.CheckConfigurationResponse
import de.idealo.projectoverviewhackday.api.model.CheckResponse
import de.idealo.projectoverviewhackday.api.model.ChecksRequest
import de.idealo.projectoverviewhackday.base.CheckService
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
	private val checkService: CheckService
) {

	@GetMapping
	fun getChecks(): List<CheckConfigurationResponse> {

		return checkService.getCheckConfigurations()
			.map { checkConfiguration ->
				CheckConfigurationResponse(
					name = checkConfiguration.name
				)
			}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.ACCEPTED)
	fun performChecks(@Valid @RequestBody checksRequest: ChecksRequest): List<CheckResponse> {

		return checkService.performChecks(checksRequest.repositoryName)
			.map { checkResult ->
				CheckResponse(
					checkName = checkResult.checkName!!,
					status = CheckResponse.Status.valueOf(checkResult.status.name),
					message = checkResult.message
				)
			}
	}
}
