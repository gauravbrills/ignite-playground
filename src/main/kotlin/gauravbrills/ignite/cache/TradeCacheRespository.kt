package gauravbrills.ignite.cache

import org.apache.ignite.IgniteCacheRestartingException
import org.apache.ignite.springdata.repository.IgniteRepository
import org.apache.ignite.springdata.repository.config.RepositoryConfig

@RepositoryConfig(cacheName = "tradeCache")
interface TradeCacheRespository : IgniteRepository<Trade, String> {
}