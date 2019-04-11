import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.apache.ignite.configuration.CacheConfiguration
import org.apache.ignite.configuration.ClientConnectorConfiguration
import org.apache.ignite.configuration.IgniteConfiguration
import org.apache.ignite.logger.slf4j.Slf4jLogger
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder
import org.springframework.boot.autoconfigure.security.SecurityProperties.User
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Arrays

@Configuration
open class IgniteConfiguraiton {

	
}