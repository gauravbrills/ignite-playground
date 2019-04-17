package gauravbrills.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.ClientConnectorConfiguration
import org.apache.ignite.configuration.DataRegionConfiguration
import org.apache.ignite.configuration.DataStorageConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.lifecycle.LifecycleBean
import org.apache.ignite.logger.slf4j.Slf4jLogger
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.apache.ignite.spi.discovery.tcp.ipfinder.s3.TcpDiscoveryS3IpFinder
import org.apache.ignite.springdata20.repository.config.EnableIgniteRepositories
import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import java.util.Arrays
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain
import com.amazonaws.ClientConfiguration

@SpringBootApplication
@EnableIgniteRepositories("gauravbrills.ignite.cache")
@Import(IgniteConfiguration::class)
open class IgnitePlaygroundApplication {

	@Bean("igniteInstance")
	open fun igniteInstance(lifeCycle: LifecycleBean): Ignite {
		val cache = CacheConfiguration<String, String>("test")
		cache.setIndexedTypes(String::class.java, String::class.java)

		val config = IgniteConfiguration().//
			setIgniteInstanceName("mydataGrid").//
			setPeerClassLoadingEnabled(false).// Code will run on any node without any need to deploy on client nodes.
			setLifecycleBeans(lifeCycle).//
			setRebalanceThreadPoolSize(1).//
			setPublicThreadPoolSize(2).//
			setSystemThreadPoolSize(2).//
			setFailureDetectionTimeout(30_000).// When node failure should be kicked in useful on a distributed cluster.
			setClientConnectorConfiguration(ClientConnectorConfiguration().setJdbcEnabled(true).setPort(1521))
			.// useful when using for Jdbc
				setCacheConfiguration(
					*IgniteConfigurationSupport.getCacheConfiguration(arrayOf("gauravbrills.ignite.cache")).toTypedArray()
				)
			.setDiscoverySpi(discoverSpi()).//  Discovery SPI
				setGridLogger(Slf4jLogger())//
		//.setConsistentId("node-1");
// setup default Data region config similar custom regions can be created 
		var dataStorageRegion = DataStorageConfiguration()
		dataStorageRegion.getDefaultDataRegionConfiguration().setMaxSize(300 * 1024 * 1024)
			.setInitialSize(200 * 1024 * 1024)
		dataStorageRegion.setDataRegionConfigurations(
			DataRegionConfiguration()//
				.setName("small").setMaxSize(200 * 1024 * 1024).setInitialSize(100 * 1024 * 1024)
		)

		config.setDataStorageConfiguration(dataStorageRegion)

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

	// Config for AWS Discovery Spi
	fun awsSpi(): TcpDiscoverySpi {
		return TcpDiscoverySpi()//
			.setIpFinder(
				TcpDiscoveryS3IpFinder().setAwsCredentialsProvider(DefaultAWSCredentialsProviderChain())
					.setBucketName("ignite-bucket")
					.setClientConfiguration(ClientConfiguration())
			)//
			.setLocalPort(47500);
	}
}

fun main(args: Array<String>) {
	runApplication<IgnitePlaygroundApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}
 