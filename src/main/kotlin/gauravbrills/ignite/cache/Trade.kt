@file:JvmName("Trade")

package gauravbrills.ignite.cache

import gauravbrills.ignite.IgniteCache
import org.apache.ignite.cache.affinity.AffinityKeyMapped
import org.apache.ignite.cache.query.annotations.QuerySqlField
import org.apache.ignite.cache.query.annotations.QueryTextField
import org.springframework.data.annotation.Id
import java.math.BigDecimal


@IgniteCache(
	value = "tradeCache", backups = 0, writeBehind = true, cacheStoreAdapter = TradeCacheStoreAdapter::class
)
data class Trade(
	@Id @QuerySqlField(index = true)
	var tradeId: String? = null,
	@AffinityKeyMapped
	@QuerySqlField(index = true)
	var isin: String? = null,
	@QuerySqlField(index = true)
	var symbol: String? = null,
	@QuerySqlField(index = true)
	var price: BigDecimal,
	@QuerySqlField(index = true)
	var volume: Int,
	// Text query lucene index shall be created
	@QueryTextField
	var comment: String
) {

}