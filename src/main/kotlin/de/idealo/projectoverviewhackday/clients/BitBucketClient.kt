package de.idealo.projectoverviewhackday.clients

import de.idealo.projectoverviewhackday.clients.model.PageableEntity
import de.idealo.projectoverviewhackday.clients.model.RepositoryEntity
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import java.util.Optional

interface BitBucketClient {

	@RequestLine("GET /rest/api/1.0/projects/{projectKey}/repos?limit=9999")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	@Cacheable("bitbucket_repositories", sync = true)
	fun getRepositories(
		@Param("projectKey") project: String
	): PageableEntity<RepositoryEntity>

	@RequestLine("GET /rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/raw/{path}")
	@Cacheable("bitbucket_raw_file", sync = true)
	fun getRawFile(
		@Param("projectKey") project: String,
		@Param("repositorySlug") repository: String,
		@Param("path", encoded = true) path: String
	): Optional<String>
}
