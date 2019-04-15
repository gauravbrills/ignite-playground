package gauravbrills.ignite

import org.apache.ignite.cache.CacheMode
import org.apache.ignite.cache.QueryEntity
import   org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.cache.CacheKeyConfiguration
import org.apache.ignite.IgniteCache
import org.apache.ignite.Ignite
import org.apache.ignite.Ignition

class CacheBuilder(
	var cacheName: String? = null,
	var cacheMode: CacheMode = CacheMode.PARTITIONED,
	var backups: Int = 1,
	var queryParallism: Int = 1,
	var expiryDuration: Long?,
	var dataRegionName: String?,
	var transactional: Boolean = false,
	var sqlOnHeapCacheEnabled: Boolean = false,
	var queryEntities: Collection<QueryEntity>?,
	var keyType: Class<*> = String::class.java,
	var valType: Class<*> = String::class.java,
	var sqlOnHeapCacheSize: Int = CacheConfiguration.DFLT_SQL_ONHEAP_CACHE_MAX_SIZE,
	var cacheKeyConfigurations: List<CacheKeyConfiguration>?
) {

	fun cacheName(cacheName: String) = apply { this.cacheName = cacheName }

	fun <k, v> build(): IgniteCache<k, v> {
		var cacheConfig = CacheConfiguration<k, v>(this.cacheName)//


		return Ignition.ignite("mydataGrid").getOrCreateCache(cacheConfig)
	}


}