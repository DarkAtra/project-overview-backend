package de.darkatra.projectoverview.context

import de.darkatra.projectoverview.api.LoggingAware
import de.darkatra.projectoverview.resolution.ArgumentResolverRegistry
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner
import org.springframework.core.env.MapPropertySource
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.stereotype.Component
import java.net.URL
import java.net.URLClassLoader
import java.util.Properties
import de.darkatra.projectoverview.api.annotation.Plugin as PluginAnnotation

@Component
class PluginManager(
	private val applicationContext: ApplicationContext,
	private val argumentResolverRegistry: ArgumentResolverRegistry,
	private val pluginProperties: PluginProperties
) : LoggingAware(), DisposableBean {

	private val plugins: MutableList<Plugin> = mutableListOf()

	override fun destroy() {
		plugins.toList().forEach { plugin -> unload(plugin) }
	}

	fun createInvokablePluginTarget(plugin: Plugin): InvokablePluginTarget {
		return InvokablePluginTarget(
			plugin = plugin,
			argumentResolverRegistry = argumentResolverRegistry
		)
	}

	fun load(pluginUrl: URL): Plugin {

		log.info("Extracting plugin metadata from url: '${pluginUrl.toExternalForm()}'")

		val pluginClassLoader = URLClassLoader(arrayOf(pluginUrl), applicationContext.classLoader)

		// read and parse plugin.properties
		val pluginProperties = pluginClassLoader.findResource("plugin.properties").openStream()
			?.let { Properties().also { properties -> properties.load(it) } }
			?: error("Plugin '${pluginUrl.toExternalForm()}' does not contain a plugin.properties file at its root.")

		// get plugin-class
		val pluginPackageName = pluginProperties.getProperty("package")
			?: error("Plugin '${pluginUrl.toExternalForm()}' does not specify the required 'package' property in its plugin.properties file.")

		// create plugin application context
		val pluginContext = AnnotationConfigApplicationContext()
		pluginContext.classLoader = pluginClassLoader

		// scan beans
		val scanner = ClassPathBeanDefinitionScanner(pluginContext, true)
		scanner.addIncludeFilter(AnnotationTypeFilter(PluginAnnotation::class.java))
		scanner.scan(pluginPackageName)

		// ensure exactly one plugin definition exists
		val pluginBeanDefinitions = scanner.registry.beanDefinitionNames
			.map(scanner.registry::getBeanDefinition)
			.filterIsInstance(AnnotatedBeanDefinition::class.java)
			.filter { it.metadata.hasAnnotation(PluginAnnotation::class.java.name) }
		when {
			pluginBeanDefinitions.isEmpty() -> error("No class is annotated with '${PluginAnnotation::class.qualifiedName}' in '${pluginUrl.toExternalForm()}'.")
			pluginBeanDefinitions.size > 1 -> error("More than one class is annotated with '${PluginAnnotation::class.qualifiedName}' in '${pluginUrl.toExternalForm()}'.")
		}

		// read plugin metadata
		val pluginBeanDefinition = pluginBeanDefinitions.first()
		val annotationAttributes = pluginBeanDefinition.metadata.getAnnotationAttributes(PluginAnnotation::class.java.name)
			?: throw  IllegalStateException("No annotation metadata found for class '${pluginBeanDefinition.beanClassName}' in '${pluginUrl.toExternalForm()}'.")

		val pluginName = annotationAttributes["name"].toString()
		val pluginAuthor = annotationAttributes["author"].toString()

		pluginContext.displayName = "'$pluginName' by '$pluginAuthor' - ${pluginBeanDefinition.beanClassName}"

		// inherit plugin specific configuration properties from the parent context
		pluginContext.environment.propertySources.addFirst(getPluginPropertySource(pluginName))

		log.info("Loading plugin '$pluginName' maintained by '$pluginAuthor' from url: '${pluginUrl.toExternalForm()}'")
		pluginContext.refresh()
		log.info("Plugin '$pluginName' was loaded successfully.")

		return Plugin(
			applicationContext = pluginContext,
			packageName = pluginPackageName,
			bean = pluginContext.getBeansWithAnnotation(PluginAnnotation::class.java).values.first(),
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

	private fun getPluginPropertySource(pluginName: String): MapPropertySource {
		return MapPropertySource("plugin-properties", pluginProperties.plugins.getOrDefault(pluginName, emptyMap()))
	}
}
