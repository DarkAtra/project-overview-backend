package de.darkatra.projectoverview.api

import de.darkatra.projectoverview.api.model.CheckConfigurationResponse
import de.darkatra.projectoverview.api.model.CheckConfigurationUpsertRequest
import de.darkatra.projectoverview.api.model.CheckResultResponse
import de.darkatra.projectoverview.api.model.CheckToRepositoryLinkRequest
import de.darkatra.projectoverview.api.model.PerformChecksRequest
import de.darkatra.projectoverview.base.CheckService
import de.darkatra.projectoverview.base.model.CheckConfiguration
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import javax.validation.constraints.NotBlank

@Validated
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
					name = checkConfiguration.name,
					type = checkConfiguration.type,
					additionalProperties = checkConfiguration.additionalProperties
				)
			}
	}

	@PutMapping("/{checkName}")
	fun upsertCheckConfiguration(@NotBlank @PathVariable checkName: String,
	                             @Valid @RequestBody checkConfigurationUpsertRequest: CheckConfigurationUpsertRequest): CheckConfigurationResponse {

		val savedCheckConfiguration = checkService.upsertCheckConfiguration(CheckConfiguration(
			name = checkName,
			type = checkConfigurationUpsertRequest.type!!,
			additionalProperties = checkConfigurationUpsertRequest.additionalProperties ?: emptyMap()
		))

		return CheckConfigurationResponse(
			name = savedCheckConfiguration.name,
			type = savedCheckConfiguration.type,
			additionalProperties = savedCheckConfiguration.additionalProperties
		)
	}

	@PostMapping("/{checkName}/repositories")
	fun linkCheckToRepository(@NotBlank @PathVariable checkName: String,
	                          @Valid @RequestBody checkToRepositoryLinkRequest: CheckToRepositoryLinkRequest) {

		checkService.linkCheckConfigurationWithRepository(
			checkName = checkName,
			repositoryName = checkToRepositoryLinkRequest.repositoryName
		)
	}

	@GetMapping("/{checkName}/results")
	fun getCheckResults(@NotBlank @PathVariable checkName: String,
	                    @NotBlank @RequestParam repositoryName: String): List<CheckResultResponse> {

		return checkService.getCheckResults(checkName = checkName, repositoryName = repositoryName)
			.map { checkResult ->
				CheckResultResponse(
					checkName = checkResult.checkName,
					status = CheckResultResponse.Status.valueOf(checkResult.status.name),
					message = checkResult.message
				)
			}
	}

	@PostMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun performChecks(@Valid @RequestBody performChecksRequest: PerformChecksRequest): List<CheckResultResponse> {

		return checkService.performChecks(performChecksRequest.repositoryName)
			.map { checkResult ->
				CheckResultResponse(
					checkName = checkResult.checkName,
					status = CheckResultResponse.Status.valueOf(checkResult.status.name),
					message = checkResult.message
				)
			}
	}
}
