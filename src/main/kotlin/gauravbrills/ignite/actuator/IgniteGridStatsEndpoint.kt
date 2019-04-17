package gauravbrills.ignite.actuator

import org.apache.ignite.Ignite
import org.apache.ignite.IgniteCache
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.stereotype.Component
import org.apache.ignite.configuration.CacheConfiguration
import kotlin.reflect.KClass

@Component
@Endpoint(id = "dataGrid", enableByDefault = true)
class IgniteGridStatsEndpoint {
	lateinit private @Autowired
	var ignite: Ignite

	@ReadOperation
	fun igniteDataGrid(): Map<String, Any> {
		var cacheMaps = hashMapOf<String, Any>();
		for (cacheName in ignite.cacheNames()) {
			var cache = ignite.getOrCreateCache<Any, Any>(cacheName)
			cacheMaps.put(cacheName, populateMetrics(cache))
		}
		return cacheMaps
	}

	@Suppress("UNCHECKED_CAST")
	fun populateMetrics(cache: IgniteCache<Any, Any>): Map<String, Any> {
		var detailsMap = hashMapOf<String, Any>();
		detailsMap.put("metrics", cache.metrics())
		detailsMap.put(
			"entities",
			(cache.getConfiguration(CacheConfiguration::class.java as Class<CacheConfiguration<Any, Any>>))//
				.getQueryEntities()
		)
		return detailsMap
	}
}