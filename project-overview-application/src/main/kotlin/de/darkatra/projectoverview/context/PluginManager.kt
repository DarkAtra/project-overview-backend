package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.LoggingAware
import org.springframework.beans.factory.DisposableBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component
import org.springframework.util.ClassUtils
import java.net.URL
import java.net.URLClassLoader
import java.util.Properties
import de.darkatra.projectoverview.api.annotation.Plugin as PluginAnnotation

@Component
class PluginManager(
	private val applicationContext: ApplicationContext
) : LoggingAware(), DisposableBean {

	private val plugins: MutableList<Plugin> = mutableListOf()

	override fun destroy() {
		plugins.toList().forEach { plugin -> unload(plugin) }
	}

	fun load(pluginUrl: URL): Plugin {

		log.info("Extracting plugin metadata from url: '${pluginUrl.toExternalForm()}'")

		val pluginClassLoader = URLClassLoader(arrayOf(pluginUrl), applicationContext.classLoader)

		// read and parse plugin.properties
		val pluginProperties = pluginClassLoader.findResource("plugin.properties").openStream()
			?.let { Properties().also { properties -> properties.load(it) } }
			?: error("Plugin '${pluginUrl.toExternalForm()}' does not contain a 'plugin.properties' file at its root.")

		// get plugin-class
		val fullyQualifiedPluginClassName = pluginProperties.getProperty("plugin-class")
			?: error("Plugin '${pluginUrl.toExternalForm()}' does not specify the 'plugin-class' property.")

		// read plugin metadata (via Plugin annotation)
		val metadataReader = SimpleMetadataReaderFactory(pluginClassLoader).getMetadataReader(fullyQualifiedPluginClassName)
		val annotationAttributes = metadataReader.annotationMetadata.getAnnotationAttributes(PluginAnnotation::class.java.canonicalName)
			?: error("Plugin '${pluginUrl.toExternalForm()}' does not specify the '${PluginAnnotation::class.qualifiedName}' annotation.")

		val pluginName = annotationAttributes["name"].toString()
		val pluginAuthor = annotationAttributes["author"].toString()

		log.info("Loading plugin '$pluginName' maintained by '$pluginAuthor' from url: '${pluginUrl.toExternalForm()}'")

		// create plugin application context
		val pluginContext = AnnotationConfigApplicationContext()
		pluginContext.classLoader = pluginClassLoader
		pluginContext.parent = applicationContext
		pluginContext.displayName = "$pluginName by $pluginAuthor - $fullyQualifiedPluginClassName"

		// scan beans
		val scanner = ClassPathBeanDefinitionScanner(pluginContext, true)
		scanner.addIncludeFilter(AnnotationTypeFilter(PluginAnnotation::class.java))

		// find and register beans that match the filters
		scanner.scan(ClassUtils.getPackageName(fullyQualifiedPluginClassName))

		// start the plugin context
		pluginContext.refresh()

		log.info("Plugin '$pluginName' was loaded successfully.")

		return Plugin(
			applicationContext = pluginContext,
			properties = pluginProperties,
			name = pluginName,
			author = pluginAuthor
		).also { plugins.add(it) }
	}

	fun unload(plugin: Plugin) {

		log.info("Unloading plugin '${plugin.name}' maintained by '${plugin.author}'.")

		plugin.applicationContext.close()
		plugins.remove(plugin)

		log.info("Plugin '${plugin.name}' was unloaded successfully.")
	}
}
