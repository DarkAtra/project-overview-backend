package de.idealo.projectoverviewhackday.clients.gitea

import de.idealo.projectoverviewhackday.clients.gitea.model.RepositoryEntity
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import java.util.Optional

interface GiteaClient {

	@RequestLine("GET /api/v1/orgs/{project}/repos")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	@Cacheable("gitea_repositories", sync = true)
	fun getRepositories(
		@Param("project") project: String
	): Optional<List<RepositoryEntity>>

	@RequestLine("GET /api/v1/repos/{project}/{repository}/raw/{path}")
	@Cacheable("gitea_raw_file", sync = true)
	fun getRawFile(
		@Param("project") project: String,
		@Param("repository") repository: String,
		@Param("path", encoded = true) path: String
	): Optional<String>
}
