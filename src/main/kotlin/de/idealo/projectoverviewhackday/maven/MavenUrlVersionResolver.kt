package de.idealo.projectoverviewhackday.maven

import org.springframework.web.util.UriComponentsBuilder
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class MavenUrlVersionResolver(
	private val url: URL,
	private val compareToLatest: Boolean
) : VersionResolver {

	override fun resolve(groupId: String, artifactId: String): String {

		val uri = UriComponentsBuilder.newInstance()
			.uri(url.toURI())
			.path("/${groupId.replace('.', '/')}")
			.path("/$artifactId")
			.path("/maven-metadata.xml")
			.build()
			.toUri()

		return DocumentBuilderFactory.newInstance().newDocumentBuilder()
			.parse(uri.toASCIIString())
			.getElementsByTagName(if (compareToLatest) "latest" else "release")
			.item(0)
			.textContent
	}
}
