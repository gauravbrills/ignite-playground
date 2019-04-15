package gauravbrills.ignite.cache

import org.apache.ignite.cache.query.annotations.QuerySqlField
import org.apache.ignite.cache.query.annotations.QueryTextField
import java.math.BigDecimal

data class Trade(
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