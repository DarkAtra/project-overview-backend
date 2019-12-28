package de.idealo.projectoverviewhackday.clients.gitea

import de.idealo.projectoverviewhackday.clients.common.MavenPomParser
import de.idealo.projectoverviewhackday.clients.common.OpenShiftPropertyParser
import de.idealo.projectoverviewhackday.clients.common.OpenShiftPropertyTarget
import de.idealo.projectoverviewhackday.clients.common.RepositoryAdapter
import de.idealo.projectoverviewhackday.clients.gitea.model.RepositoryEntity
import de.idealo.projectoverviewhackday.model.Property
import de.idealo.projectoverviewhackday.model.Repository
import de.idealo.projectoverviewhackday.model.merge
import org.apache.maven.model.Model
import org.springframework.cache.annotation.CacheEvict
import org.springframework.scheduling.annotation.Scheduled
import java.util.Locale

open class GiteaRepositoryAdapter(
	private val giteaClient: GiteaClient,
	private val giteaAdapterProperties: GiteaAdapterProperties,
	private val mavenPomParser: MavenPomParser,
	private val openShiftPropertyParser: OpenShiftPropertyParser
) : RepositoryAdapter {

	override fun getRepositories(project: String): List<Repository> {

		return giteaClient.getRepositories(project)
			.orElse(emptyList())
			.map { repositoryEntity ->
				Repository.Builder(repositoryEntity.name, repositoryEntity.owner.name, getRepositoryUrl(repositoryEntity))
					.model(getPom(repositoryEntity))
					.openShiftProperties(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PROJECT)
						.merge(getOpenShiftProperties(repositoryEntity, OpenShiftPropertyTarget.PRODUCTION)))
			}
			.map(Repository.Builder::build)
	}

	private fun getRepositoryUrl(repositoryEntity: RepositoryEntity): String {

		return "${giteaAdapterProperties.url!!}/${repositoryEntity.owner.name}/${repositoryEntity.name}"
	}

	private fun getPom(repositoryEntity: RepositoryEntity): Model? {

		return giteaClient.getRawFile(repositoryEntity.owner.name, repositoryEntity.name, "pom.xml")
			.map { pom -> mavenPomParser.parse(pom) }
			.orElse(null)
	}

	private fun getOpenShiftProperties(repositoryEntity: RepositoryEntity, openShiftPropertyTarget: OpenShiftPropertyTarget): List<Property> {

		val path = "scm/openshift/${openShiftPropertyTarget.name.toLowerCase(Locale.ENGLISH)}.properties"
		return giteaClient.getRawFile(repositoryEntity.owner.name, repositoryEntity.name, path)
			.map { openShiftProperties -> openShiftPropertyParser.parse(openShiftProperties) }
			.orElse(emptyList())
	}

	@Scheduled(cron = "0 0/30 * * * ?")
	@CacheEvict("gitea_repositories", "gitea_raw_file", allEntries = true)
	open fun refreshCache() {
		// only used to evict the bitbucket cache
	}
}
