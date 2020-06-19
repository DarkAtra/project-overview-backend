package de.idealo.projectoverviewhackday.maven

import de.idealo.projectoverviewhackday.base.VersionParser
import de.idealo.projectoverviewhackday.base.model.Version
import org.springframework.web.util.UriComponentsBuilder
import java.net.URL
import java.text.ParseException
import javax.xml.parsers.DocumentBuilderFactory

class MavenUrlVersionResolver(
	private val url: URL,
	private val versionParser: VersionParser,
	private val compareToLatest: Boolean
) : VersionResolver {

	override fun resolve(groupId: String, artifactId: String): Version {

		val uri = UriComponentsBuilder.newInstance()
			.uri(url.toURI())
			.path("/${groupId.replace('.', '/')}")
			.path("/$artifactId")
			.path("/maven-metadata.xml")
			.build()
			.toUri()

		val versionString = DocumentBuilderFactory.newInstance().newDocumentBuilder()
			.parse(uri.toASCIIString())
			.getElementsByTagName(if (compareToLatest) "latest" else "release").item(0).textContent

		// FIXME: think about what should happen if a version could not be parsed
		return versionParser.parse(versionString) ?: throw ParseException("Could not parse version '$versionString'", 0)
	}
}
