package de.idealo.projectoverviewhackday.maven

import de.idealo.projectoverviewhackday.base.LoggingAware
import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.time.Instant

@Component
class MavenModelResolver(
	private val mavenModelResolverProperties: MavenModelResolverProperties
) : LoggingAware() {

	private var processBuilder = ProcessBuilder()
	private var reader = MavenXpp3Reader()

	@Cacheable(cacheNames = ["maven_model"], key = "#directory.toAbsolutePath().toString()")
	fun getModel(directory: Path): Model {

		val workingDirectory = createAndGetTempDir(directory)
		val pomPath = directory.resolve("pom.xml")
		val effectivePomPath = workingDirectory.resolve("effective-pom.xml")

		val process = processBuilder
			.command(mavenModelResolverProperties.executable.toString(), "help:effective-pom", "--file=${pomPath.toAbsolutePath()}", "-Doutput=${effectivePomPath.toAbsolutePath()}")
			.directory(workingDirectory.toFile())
			.also {
				val logFile = workingDirectory.resolve("mvn.log").toFile()
				it.redirectOutput(logFile)
				it.redirectError(logFile)
			}
			.start()

		process.waitFor()

		if (process.exitValue() != 0) {
			throw ModelResolutionException("Could not generate effective pom for maven project '${directory.toAbsolutePath()}'.")
		}

		return reader.read(effectivePomPath.toFile().reader())
	}

	@CacheEvict(cacheNames = ["maven_model"], allEntries = true)
	@Scheduled(fixedDelay = 5 * 60 * 1000, initialDelay = 5 * 60 * 1000)
	fun evictCache() {
		log.info("Flushed 'maven_model' cache: ${Instant.now()}")
	}

	private fun createAndGetTempDir(directory: Path): Path {
		return mavenModelResolverProperties.tempDir.resolve(directory.fileName).also { it.toFile().mkdirs() }
	}
}
