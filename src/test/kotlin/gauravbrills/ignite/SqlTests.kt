package gauravbrills.ignite

import gauravbrills.ignite.cache.Trade
import org.apache.ignite.Ignite
import org.apache.ignite.cache.CacheMode
import org.apache.ignite.cache.query.SqlQuery
import org.apache.ignite.configuration.CacheConfiguration
import org.junit.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import reactor.core.publisher.Flux
import java.math.BigDecimal

private val log = LoggerFactory.getLogger(SqlTests::class.java)

open class SqlTests : IgnitePlagroundApplicationTests() {
	@Autowired
	lateinit var ignite: Ignite;

	@Test
	fun runAllTypesOfQueries() {
		// Create Caches
		var tradeCacheConfig = CacheConfiguration<String, Trade>("tradeCache");
		tradeCacheConfig.setCacheMode(CacheMode.REPLICATED);
		tradeCacheConfig.setIndexedTypes(String::class.java, Trade::class.java);

		ignite.getOrCreateCache(tradeCacheConfig);

		loadCaches()

		sqlQuery();
	}


	fun loadCaches() {
		val tradeCache = ignite.cache<String, Trade>("tradeCache");
		tradeCache.clear()
		Flux.just(
			Trade(
				isin = "US-000402625-0",
				symbol = "Apple Inc",
				price = BigDecimal(198.87),
				volume = 1000,
				comment = "Sell It"
			),
			Trade(
				isin = "JP-000K0VF05-4",
				symbol = "Honda Motor",
				price = BigDecimal(3085.00),
				volume = 1212,
				comment = "Sell It"
			),
			Trade(
				isin = "US38259P5089",
				symbol = "Alphabet",
				price = BigDecimal(1223.99),
				volume = 454,
				comment = "Buy It, on the rise"
			),
			Trade(
				isin = "US0231351067",
				symbol = "Amazon Inc",
				price = BigDecimal(1330),
				volume = 1200,
				comment = "Buy It"
			)
		).map {
			tradeCache.put(it.isin, it)
			it.isin
		}.map {
			tradeCache.get(it)
		}.subscribe {
			log.info(it.toString())
		}
	}

	fun sqlQuery() {
		var cache = ignite.cache<String, Trade>("tradeCache");

		// Create query which selects salaries based on range.
		var qry = SqlQuery<String, Trade>(Trade::class.java, "volume > ?");


		log.info("Trades where volume traded > 1000: {}", cache.query(qry.setArgs(1000)).getAll());
	}
}