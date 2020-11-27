package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.annotation.Plugin
import de.darkatra.projectoverview.api.annotation.PluginConfiguration
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar
import org.springframework.core.env.Environment
import org.springframework.core.io.ResourceLoader
import org.springframework.core.type.AnnotationMetadata
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.Assert

class PluginConfigurationBeanRegistrar(
	private val resourceLoader: ResourceLoader,
	private val environment: Environment
) : ImportBeanDefinitionRegistrar {

	override fun registerBeanDefinitions(importingClassMetadata: AnnotationMetadata, registry: BeanDefinitionRegistry) {

		val scanner = getScanner()

		scanner.findCandidateComponents("de.darkatra.*")
			.filterIsInstance(AnnotatedBeanDefinition::class.java)
			.map(AnnotatedBeanDefinition::getMetadata)
			.forEach { annotationMetadata ->
				Assert.isTrue(annotationMetadata.isConcrete, "${PluginConfiguration::class.qualifiedName} annotation can only be specified on a concrete class.")

				val attributes = annotationMetadata.getAnnotationAttributes(PluginConfiguration::class.java.canonicalName)

				// register bean definition
			}
	}

	private fun getScanner(): ClassPathScanningCandidateComponentProvider {
		return object : ClassPathScanningCandidateComponentProvider(false, this.environment) {
			override fun isCandidateComponent(beanDefinition: AnnotatedBeanDefinition): Boolean {
				return beanDefinition.metadata.isIndependent && !beanDefinition.metadata.isAnnotation
			}
		}.also { scanner ->
			scanner.resourceLoader = resourceLoader
			scanner.addIncludeFilter(AnnotationTypeFilter(PluginConfiguration::class.java))
		}
	}
}
