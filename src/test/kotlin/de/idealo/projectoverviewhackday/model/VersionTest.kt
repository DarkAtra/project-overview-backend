package de.idealo.projectoverviewhackday.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class VersionTest {

	private val version: Version = Version(
		major = 1,
		minor = 1,
		build = 1
	)

	@Test
	internal fun compareShouldResultInNotFound() {

		val checkOutcome = version.compare(null)

		assertThat(checkOutcome).isEqualTo(CheckOutcome.NOT_FOUND)
	}

	@Test
	internal fun compareShouldResultInOutdated() {

		listOf(
			Version(major = 2, minor = 1, build = 1),
			Version(major = 2, minor = 2, build = 1),
			Version(major = 2, minor = 1, build = 2),
			Version(major = 2, minor = 2, build = 2),
			Version(major = 1, minor = 2, build = 1),
			Version(major = 1, minor = 1, build = 2),
			Version(major = 1, minor = 2, build = 2)
		).forEach { wantedVersion ->
			val checkOutcome = version.compare(wantedVersion)

			assertThat(checkOutcome)
				.describedAs("Comparing version: $version with wantedVersion: $wantedVersion")
				.isEqualTo(CheckOutcome.OUTDATED)
		}
	}

	@Test
	internal fun compareShouldResultInVeryOutdated() {

		listOf(
			Version(major = 3, minor = 1, build = 1),
			Version(major = 3, minor = 0, build = 1),
			Version(major = 3, minor = 1, build = 0),
			Version(major = 3, minor = 2, build = 1),
			Version(major = 3, minor = 1, build = 2)
		).forEach { wantedVersion ->
			val checkOutcome = version.compare(wantedVersion)

			assertThat(checkOutcome)
				.describedAs("Comparing version: $version with wantedVersion: $wantedVersion")
				.isEqualTo(CheckOutcome.VERY_OUTDATED)
		}
	}

	@Test
	internal fun compareShouldResultInUpToDate() {

		listOf(
			version,
			Version(major = 1, minor = 1, build = 1),
			Version(major = 1, minor = 1, build = 0),
			Version(major = 1, minor = 0, build = 1),
			Version(major = 0, minor = 1, build = 1),
			Version(major = 1, minor = 0, build = 2),
			Version(major = 0, minor = 1, build = 2),
			Version(major = 0, minor = 2, build = 1)
		).forEach { wantedVersion ->
			val checkOutcome = version.compare(wantedVersion)

			assertThat(checkOutcome)
				.describedAs("Comparing version: $version with wantedVersion: $wantedVersion")
				.isEqualTo(CheckOutcome.UP_TO_DATE)
		}
	}
}