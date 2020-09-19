package de.darkatra.projectoverview.maven

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.validation.annotation.Validated
import java.nio.file.Path
import javax.validation.constraints.NotNull

@Validated
@ConstructorBinding
@ConfigurationProperties(prefix = "projectoverview.maven")
data class MavenModelResolverProperties(
	@NotNull
	val executable: Path,
	@NotNull
	val tempDir: Path
)
