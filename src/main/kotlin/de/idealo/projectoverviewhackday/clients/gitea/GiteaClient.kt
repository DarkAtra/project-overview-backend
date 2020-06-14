package de.idealo.projectoverviewhackday.clients.gitea

import de.idealo.projectoverviewhackday.clients.gitea.model.RepositoryEntity
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.http.MediaType
import java.util.Optional

interface GiteaClient {

	@RequestLine("GET /api/v1/orgs/{project}/repos")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	fun getRepositories(
		@Param("project") project: String
	): Optional<List<RepositoryEntity>>
}
