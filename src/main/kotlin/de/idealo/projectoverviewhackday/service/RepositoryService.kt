package de.idealo.projectoverviewhackday.service

import de.idealo.projectoverviewhackday.clients.BitBucketClient
import de.idealo.projectoverviewhackday.clients.model.OpenShiftPropertyTarget
import de.idealo.projectoverviewhackday.clients.model.RepositoryEntity
import de.idealo.projectoverviewhackday.model.Artifact
import de.idealo.projectoverviewhackday.model.CheckResult
import de.idealo.projectoverviewhackday.model.DependencyCheck
import de.idealo.projectoverviewhackday.model.OpenShiftPropertyCheck
import de.idealo.projectoverviewhackday.model.ParentCheck
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

	private val checks = listOf(
		ParentCheck(
			displayName = "idealo Parent Pom",
			artifact = Artifact(
				groupId = "de.idealo.common.spring-boot",
				artifactId = "idealo-spring-boot-parent",
				version = "8.1.1"
			)
		),
		DependencyCheck(
			displayName = "Business Lib Common Starter",
			artifact = Artifact(
				groupId = "de.idealo.businesslib",
				artifactId = "idealo-businesslib-starter-common",
				version = "1.41.0"
			)
		),
		DependencyCheck(
			displayName = "Business Lib AWS Starter",
			artifact = Artifact(
				groupId = "de.idealo.businesslib",
				artifactId = "idealo-businesslib-starter-aws",
				version = "1.41.0"
			)
		),
		OpenShiftPropertyCheck(
			displayName = "Deployment Config",
			property = Property(
				key = "PROMETHEUS_SCRAPING_ENABLED",
				value = "true"
			)
		),
		OpenShiftPropertyCheck(
			displayName = "Grafana Cloud",
			property = Property(
				key = "DEPLOYMENT_TYPE",
				value = "deploymentconfig"
			)
		)
	)

	fun getRepositories(): List<Repository> {
		return bitBucketClient.getRepositories(repositoryServiceProperties.project!!).values
			.filter { repositoryEntity -> !repositoryServiceProperties.excludedRepositories.contains(repositoryEntity.name) }
			.map { repositoryEntity ->
				bitBucketClient.getPom(repositoryEntity.project.key, repositoryEntity.slug)
					.map { pom -> Repository.Builder.of(repositoryEntity.name, repositoryEntity.project.key, mavenPomParser.parse(pom)) }
					.orElse(Repository.Builder(repositoryEntity.name, repositoryEntity.project.key))
					.openShiftProperties(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PROJECT)
						.merge(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PRODUCTION)))
			}
			.map(Repository.Builder::build)
			.onEach(this::performChecks)
	}

	private fun getOpenShiftProperties(repositoryEntity: RepositoryEntity, openShiftPropertyTarget: OpenShiftPropertyTarget): List<Property> {
		return bitBucketClient.getOpenshiftProperties(repositoryEntity.project.key, repositoryEntity.slug, openShiftPropertyTarget)
			.map { openShiftProperties -> openShiftPropertyParser.parse(openShiftProperties) }
			.orElse(emptyList())
	}

	private fun performChecks(repository: Repository) {
		checks.forEach { check ->
			repository.checkResults.add(
				CheckResult(
					check = check,
					checkOutcome = check.check(repository)
				)
			)
		}
	}
}
