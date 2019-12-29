package de.idealo.projectoverviewhackday.clients.bitbucket

import de.idealo.projectoverviewhackday.clients.bitbucket.model.RepositoryEntity
import de.idealo.projectoverviewhackday.clients.common.MavenPomParser
import de.idealo.projectoverviewhackday.clients.common.OpenShiftPropertyParser
import de.idealo.projectoverviewhackday.clients.common.OpenShiftPropertyTarget
import de.idealo.projectoverviewhackday.clients.common.RepositoryAdapter
import de.idealo.projectoverviewhackday.model.Property
import de.idealo.projectoverviewhackday.model.Repository
import de.idealo.projectoverviewhackday.model.Repository.RepositoryBuilder
import de.idealo.projectoverviewhackday.model.merge
import org.apache.maven.model.Model
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import java.util.Locale

open class BitBucketRepositoryAdapter(
	private val bitBucketClient: BitBucketClient,
	private val bitBucketAdapterProperties: BitBucketAdapterProperties,
	private val mavenPomParser: MavenPomParser,
	private val openShiftPropertyParser: OpenShiftPropertyParser
) : RepositoryAdapter {

	override fun getRepositories(project: String): List<Repository> {

		return bitBucketClient.getRepositories(project).values
			.map { repositoryEntity ->
				RepositoryBuilder(repositoryEntity.name, repositoryEntity.project.name, getRepositoryUrl(repositoryEntity))
					.model(getPom(repositoryEntity))
					.openShiftProperties(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PROJECT)
						.merge(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PRODUCTION)))
			}
			.map(RepositoryBuilder::build)
	}

	private fun getRepositoryUrl(repositoryEntity: RepositoryEntity): String {

		return "${bitBucketAdapterProperties.url!!}/projects/${repositoryEntity.project.name}/repos/${repositoryEntity.name}/browse"
	}

	private fun getPom(repositoryEntity: RepositoryEntity): Model? {

		return bitBucketClient.getRawFile(repositoryEntity.project.name, repositoryEntity.id, "pom.xml")
			.map { pom -> mavenPomParser.parse(pom) }
			.orElse(null)
	}

	private fun getOpenShiftProperties(repositoryEntity: RepositoryEntity, openShiftPropertyTarget: OpenShiftPropertyTarget): List<Property> {

		val path = "scm/openshift/${openShiftPropertyTarget.name.toLowerCase(Locale.ENGLISH)}.properties"
		return bitBucketClient.getRawFile(repositoryEntity.project.name, repositoryEntity.id, path)
			.map { openShiftProperties -> openShiftPropertyParser.parse(openShiftProperties) }
			.orElse(emptyList())
	}

	@Scheduled(cron = "0 0/30 * * * ?")
	@CacheEvict("bitbucket_repositories", "bitbucket_raw_file", allEntries = true)
	open fun refreshCache() {
		// only used to evict the bitbucket cache
	}
}
