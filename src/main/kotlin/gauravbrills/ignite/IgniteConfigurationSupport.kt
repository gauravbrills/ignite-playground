package gauravbrills.ignite

import org.apache.ignite.configuration.CacheConfiguration
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider

class IgniteConfigurationSupport() {

	fun getCacheConfiguration(basePackages: Array<String>): CacheConfiguration<*, *> {
//var caches = ClassPathScanningCandidateComponentProvider().
		return CacheConfiguration<Any, Any>("tradeCache");
	}

}