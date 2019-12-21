package de.idealo.projectoverviewhackday.service

import de.idealo.projectoverviewhackday.clients.BitBucketClient
import de.idealo.projectoverviewhackday.clients.model.OpenShiftPropertyTarget
import de.idealo.projectoverviewhackday.clients.model.RepositoryEntity
import de.idealo.projectoverviewhackday.model.Check
import de.idealo.projectoverviewhackday.model.Property
import de.idealo.projectoverviewhackday.model.Repository
import de.idealo.projectoverviewhackday.model.merge
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.stereotype.Service

@Service
@EnableConfigurationProperties(RepositoryServiceProperties::class)
class RepositoryService(
	private val repositoryServiceProperties: RepositoryServiceProperties,
	private val bitBucketClient: BitBucketClient,
	private val mavenPomParser: MavenPomParser,
	private val openShiftPropertyParser: OpenShiftPropertyParser
) {

	fun getRepositories(): List<Repository> {
		return bitBucketClient.getRepositories(repositoryServiceProperties.project!!).values
			.map { repositoryEntity ->
				Repository.Builder(repositoryEntity.name, repositoryEntity.project.key)
					.model(bitBucketClient.getPom(repositoryEntity.project.key, repositoryEntity.slug)
						.map { pom -> mavenPomParser.parse(pom) }
						.orElse(null))
					.openShiftProperties(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PROJECT)
						.merge(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PRODUCTION)))
			}
			.map(Repository.Builder::build)
			.onEach(this::performChecks)
			.filter { it.checkResults.isNotEmpty() }
	}

	private fun getOpenShiftProperties(repositoryEntity: RepositoryEntity, openShiftPropertyTarget: OpenShiftPropertyTarget): List<Property> {
		return bitBucketClient.getOpenshiftProperties(repositoryEntity.project.key, repositoryEntity.slug, openShiftPropertyTarget)
			.map { openShiftProperties -> openShiftPropertyParser.parse(openShiftProperties) }
			.orElse(emptyList())
	}

	private fun performChecks(repository: Repository) {
		getChecks(repository).map { it.check(repository) }.forEach { repository.checkResults.add(it) }
	}

	private fun getChecks(repository: Repository): List<Check<out Any>> {

		val excludedChecks = this.repositoryServiceProperties.checkOverrides!!
			.find { it.repository == repository.name }
			?.excludedChecks ?: emptyList()

		return this.repositoryServiceProperties.checks!!
			.mapNotNull { it.build(excludedChecks) }
	}
}
