package gauravbrills.ignite.cache

import org.apache.ignite.cache.query.annotations.QuerySqlField
import org.apache.ignite.cache.query.annotations.QueryTextField
import java.math.BigDecimal
import com.sun.javafx.beans.IDProperty
import org.springframework.data.annotation.Id
import gauravbrills.ignite.IgniteCache

@IgniteCache(value= "securityCache", backups = 0, dataRegion="small")
data class Security(
	@Id
	@QuerySqlField(index = true)
	var isin: String? = null,
	@QuerySqlField(index = true)
	var symbol: String? = null,
	// Equity etc
	@QuerySqlField(index = true)
	var type: String? = null,
	@QuerySqlField(index = true)
	var marketSegment: String? = null
) {

}