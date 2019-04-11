package gauravbrills.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.ClientConnectorConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.logger.slf4j.Slf4jLogger
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import java.util.Arrays

@SpringBootApplication
@Import(IgniteConfiguration::class)
open class IgnitePlaygroundApplication {
	
	@Bean("igniteInstance")
	open fun igniteInstance(): Ignite {
		val cache = CacheConfiguration<String, String>("test")
		cache.setIndexedTypes(String::class.java, String::class.java)

		val config = IgniteConfiguration().//
			setIgniteInstanceName("mydataGrid").//
			setPeerClassLoadingEnabled(true).// Code will run on any node without any need to deploy on client nodes.
			setRebalanceThreadPoolSize(1).//
			setPublicThreadPoolSize(2).//
			setSystemThreadPoolSize(2).//
			setFailureDetectionTimeout(30_000).// When node failure should be kicked in useful on a distributed cluster.
			setClientConnectorConfiguration(ClientConnectorConfiguration().setJdbcEnabled(true).setPort(1521))
			.// useful when using for Jdbc
				setCacheConfiguration(cache).//
				setDiscoverySpi(discoverSpi()).//  Discovery SPI
				setGridLogger(Slf4jLogger()).//
				setConsistentId("node-1");

		return Ignition.start(config);
	}

	fun discoverSpi(): TcpDiscoverySpi {
		return TcpDiscoverySpi()//
			.setIpFinder(
				TcpDiscoveryMulticastIpFinder().setAddresses(
					Arrays.asList(
						"127.0.0.1:47500",
						"127.0.0.1:47501"
					)
				)
			);
	}
}

fun main(args: Array<String>) {
	runApplication<IgnitePlaygroundApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}

