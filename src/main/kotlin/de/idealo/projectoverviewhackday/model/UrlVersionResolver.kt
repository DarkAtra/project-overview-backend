package de.idealo.projectoverviewhackday.model

import org.springframework.web.util.UriComponentsBuilder
import java.net.URL
import java.text.ParseException
import javax.xml.parsers.DocumentBuilderFactory


class UrlVersionResolver(
	private val url: URL,
	private val versionParser: VersionParser
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
			.getElementsByTagName("release").item(0).textContent

		// FIXME: think about what should happen if a version could not be parsed
		return versionParser.parse(versionString) ?: throw ParseException("Could not parse version '$versionString'", 0)
	}
}
