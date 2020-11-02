package de.darkatra.projectoverview.api.annotation

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@MustBeDocumented
@ConstructorBinding
@ConfigurationProperties
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class PluginConfiguration
