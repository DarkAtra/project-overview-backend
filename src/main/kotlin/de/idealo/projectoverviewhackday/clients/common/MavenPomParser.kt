package de.idealo.projectoverviewhackday.clients.common

import org.apache.maven.model.Model
import org.apache.maven.model.io.xpp3.MavenXpp3Reader
import org.springframework.stereotype.Component

@Component
class MavenPomParser(private val mavenPropertyResolver: MavenPropertyResolver) {

	private val pomParser = MavenXpp3Reader()

	fun parse(pom: String): Model? {
		return mavenPropertyResolver.resolveVersion(pomParser.read(pom.byteInputStream()))
	}
}
