package de.darkatra.projectoverview.resolution

import de.darkatra.projectoverview.api.annotation.RepositoryDirectory
import de.darkatra.projectoverview.context.CheckContext
import org.springframework.stereotype.Component
import java.lang.reflect.Parameter
import java.nio.file.Path

@Component
class RepositoryDirectoryArgumentResolver : ArgumentResolver<RepositoryDirectory> {

	override fun resolve(parameter: Parameter, checkContext: CheckContext): Path {
		return checkContext.getRepositoryDirectory()
	}
}
