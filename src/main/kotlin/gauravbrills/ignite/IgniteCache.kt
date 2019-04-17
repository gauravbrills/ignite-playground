package gauravbrills.ignite

import org.apache.ignite.cache.CacheMode
import kotlin.reflect.KClass

// Convenient annotaiton to configure cache configs
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class IgniteCache(
	val value: String,
	val backups: Int = 0,
	val cacheMode: CacheMode = CacheMode.PARTITIONED,
	val readThrough: Boolean = false,
	val writeThrough: Boolean = false,
	val writeBehind: Boolean = false,
	val cacheStoreAdapter: KClass<*> = Void::class,
	val evictionMaxSize: Int = 100_000,
	val dataRegion: String = "",
	val expiryDuration: Long = 60,
	val onHeapCache: Boolean = false,
	val writeBehindFlushThreadCount: Int = 2,
	val writeBehindBatchSize: Int = 500,
	val writeBehindFlushFrequencyInMillis: Long = 1000
) {
}