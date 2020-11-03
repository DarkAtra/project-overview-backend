package de.darkatra.projectoverview.check

import de.darkatra.projectoverview.api.LoggingAware
import de.darkatra.projectoverview.context.DefaultCheckContext
import de.darkatra.projectoverview.context.PluginManager
import de.darkatra.projectoverview.git.RepositoryService
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
@EnableScheduling
class CheckService(
	private val pluginManager: PluginManager,
	private val repositoryService: RepositoryService
) : LoggingAware() {

	private val checksToPerform = mutableListOf<Check>()

	fun queueCheck(check: Check) {
		checksToPerform.add(check)
	}

	@Scheduled(fixedDelay = 10000)
	fun performQueuedChecks() {

		log.info("Performing ${checksToPerform.size} checks.")

		checksToPerform.toList().forEach { check ->
			// TODO: handle plugin not found
			val plugin = pluginManager.getPlugin(check.pluginName)
			val repository = repositoryService.getRepository(check.repositorySlug)

			repositoryService.upsertAndGetLocalRepository(repository).use { git ->

				val checkContext = DefaultCheckContext(
					checkName = check.checkConfiguration.name,
					repositoryDirectory = git.repository.directory.toPath().resolve("..").normalize(),
					parameters = check.checkConfiguration.properties
				)

				// TODO: persists checkResult (to support displaying the history)
				val checkResult = pluginManager.createInvokablePluginTarget(plugin).performCheck("parent", checkContext)
				log.info("CheckResult for '${checkContext.getCheckName()}': ${checkResult.checkOutcome} - ${checkResult.message}")

				checksToPerform.remove(check)
			}
		}
	}
}
