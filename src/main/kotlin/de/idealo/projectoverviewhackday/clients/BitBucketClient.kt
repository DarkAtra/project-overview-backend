package de.idealo.projectoverviewhackday.clients

import de.idealo.projectoverviewhackday.clients.model.OpenShiftPropertyTarget
import de.idealo.projectoverviewhackday.clients.model.PageableEntity
import de.idealo.projectoverviewhackday.clients.model.RepositoryEntity
import feign.Headers
import feign.Param
import feign.RequestLine
import org.springframework.http.MediaType
import java.util.Optional

interface BitBucketClient {

	@RequestLine("GET /rest/api/1.0/projects/{projectKey}/repos?limit=9999")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	fun getRepositories(
		@Param("projectKey") project: String
	): PageableEntity<RepositoryEntity>

	@RequestLine("GET /rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/raw/pom.xml")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	fun getPom(
		@Param("projectKey") project: String,
		@Param("repositorySlug") repository: String
	): Optional<String>

	@RequestLine("GET /rest/api/1.0/projects/{projectKey}/repos/{repositorySlug}/raw/scm/openshift/{openShiftPropertyTarget}.properties")
	@Headers("Content-Type: ${MediaType.APPLICATION_JSON_VALUE}")
	fun getOpenshiftProperties(
		@Param("projectKey") project: String,
		@Param("repositorySlug") repository: String,
		@Param("openShiftPropertyTarget", expander = OpenShiftPropertyTarget.Expander::class) openShiftPropertyTarget: OpenShiftPropertyTarget
	): Optional<String>
}
