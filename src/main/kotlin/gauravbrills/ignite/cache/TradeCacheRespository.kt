package gauravbrills.ignite.cache

import org.apache.ignite.springdata20.repository.IgniteRepository
import org.apache.ignite.springdata20.repository.config.RepositoryConfig
import org.apache.ignite.springdata20.repository.config.Query

@RepositoryConfig(cacheName = "tradeCache")
interface TradeCacheRespository : IgniteRepository<Trade, String> {

	fun findByIsin(isin: String): List<Trade>;
	
	@Query("SELECT * FROM Trade WHERE lower(comment) like '%sell%'")
	fun findWhereInstructionToSell( ): List<Trade>;
}