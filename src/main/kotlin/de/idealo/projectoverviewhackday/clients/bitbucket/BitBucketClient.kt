package de.idealo.projectoverviewhackday.clients.bitbucket

import de.idealo.projectoverviewhackday.clients.bitbucket.model.PageableEntity
import de.idealo.projectoverviewhackday.clients.bitbucket.model.RepositoryEntity
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.http.MediaType

/**
 * Client for the Bitbucket Server API: https://docs.atlassian.com/bitbucket-server/rest/6.4.0/bitbucket-rest.html
 */
interface BitBucketClient {

	@RequestLine("GET /rest/api/1.0/projects/{projectKey}/repos?limit=9999")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	fun getRepositories(
		@Param("projectKey") project: String
	): PageableEntity<RepositoryEntity>
}
