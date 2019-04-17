package gauravbrills.ignite

import org.apache.ignite.cache.eviction.fifo.FifoEvictionPolicyFactory
import org.apache.ignite.cache.store.CacheStore
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.apache.ignite.configuration.CacheConfiguration
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AnnotationTypeFilter
import org.springframework.util.ClassUtils
import java.util.concurrent.TimeUnit
import javax.cache.configuration.FactoryBuilder
import javax.cache.expiry.CreatedExpiryPolicy
import javax.cache.expiry.Duration

/*
 * Provides helpers for configuring Ignite caches
 */
class IgniteConfigurationSupport() {
	companion object static {
		fun getCacheConfiguration(basePackages: Array<String>): List<CacheConfiguration<*, *>> {
			var cacheConfigurations = arrayListOf<CacheConfiguration<Any, Any>>();

			var scanningProvider =
				ClassPathScanningCandidateComponentProvider(false)
			scanningProvider.addIncludeFilter(AnnotationTypeFilter(IgniteCache::class.java));

			for (basePackage in basePackages) {

				scanningProvider.findCandidateComponents(basePackage)//
					.stream()//
					.map {
						ClassUtils.resolveClassName(
							it.getBeanClassName(), ClassUtils.getDefaultClassLoader()
						)
					}//
					.forEach { cacheConfigurations.add(getCacheConfig(it)) };
			}

			return cacheConfigurations;
		}

		fun getCacheConfig(cache: Class<*>): CacheConfiguration<Any, Any> {

			var annotation = cache.getAnnotation(IgniteCache::class.java)
			var config = CacheConfiguration<Any, Any>(annotation.value)
				.setBackups(annotation.backups)
				.setCacheMode(annotation.cacheMode)
				.setReadThrough(annotation.readThrough)
				.setWriteThrough(annotation.writeThrough)
				.setWriteBehindEnabled(annotation.writeBehind)
				.setIndexedTypes(String::class.java, cache)
				.setRebalanceBatchSize(1048576)
				.setRebalanceThrottle(0)

			if (annotation.dataRegion.isNotBlank()) {
				config.setDataRegionName(annotation.dataRegion)
			}

			if (annotation.onHeapCache) {
				config.setOnheapCacheEnabled(annotation.onHeapCache)
				config.setEvictionPolicyFactory(FifoEvictionPolicyFactory<Any, Any>(annotation.evictionMaxSize))
			}

			if (annotation.cacheStoreAdapter != Void::class.java && CacheStoreAdapter::class.java.isAssignableFrom(
					annotation.cacheStoreAdapter.java
				)
			) {
				config.setCacheStoreFactory(FactoryBuilder.factoryOf(annotation.cacheStoreAdapter.java as Class<CacheStore<Any, Any>>))
				// write throtlling
				config.setWriteBehindBatchSize(annotation.writeBehindBatchSize)
					.setWriteBehindFlushFrequency(annotation.writeBehindFlushFrequencyInMillis)
					.setWriteBehindFlushThreadCount(annotation.writeBehindFlushThreadCount)

			}


			config.setExpiryPolicyFactory(
				CreatedExpiryPolicy.factoryOf(
					Duration(
						TimeUnit.MINUTES,
						annotation.expiryDuration
					)
				)
			)

			return config;
		}

	}
}