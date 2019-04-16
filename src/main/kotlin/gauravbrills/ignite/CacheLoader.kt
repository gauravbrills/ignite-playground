package gauravbrills.ignite

import gauravbrills.ignite.cache.Security
import gauravbrills.ignite.cache.Trade
import org.apache.ignite.Ignite
import org.apache.ignite.Ignition
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.math.BigDecimal

private val log = LoggerFactory.getLogger(CacheLoader::class.java)

@Component
class CacheLoader() {

	fun loadCaches() {
		log.info("Populating Security Cache")
		val securityCache = Ignition.ignite("mydataGrid").cache<String, Security>("securityCache");
		securityCache.clear()
		Flux.just(
			Security(
				isin = "US-000402625-0",
				symbol = "Apple Inc",
				type = "Equity",
				marketSegment = "Openmarket"
			),
			Security(
				isin = "JP-000K0VF05-4",
				symbol = "Honda Motor",
				type = "Equity",
				marketSegment = "Openmarket"
			),
			Security(
				isin = "US38259P5089",
				symbol = "Alphabet",
				type = "Equity",
				marketSegment = "Openmarket"
			),
			Security(
				isin = "US0231351067",
				symbol = "Amazon Inc",
				type = "Equity",
				marketSegment = "Openmarket"
			)
		).map {
			securityCache.put(it.isin, it)
			it.isin
		}.map {
			securityCache.get(it)
		}.subscribe {
			log.info(it.toString())
		}

		log.info("Populating Trade Cache")
		val tradeCache = Ignition.ignite("mydataGrid").cache<String, Trade>("tradeCache");
		tradeCache.clear()
		Flux.just(
			Trade(
				tradeId = "1",
				isin = "US-000402625-0",
				symbol = "Apple Inc",
				price = BigDecimal(198.87),
				volume = 1000,
				comment = "Sell It"
			),
			Trade(
				tradeId = "2",
				isin = "JP-000K0VF05-4",
				symbol = "Honda Motor",
				price = BigDecimal(3085.00),
				volume = 1212,
				comment = "Sell It"
			),
			Trade(
				tradeId = "3",
				isin = "US38259P5089",
				symbol = "Alphabet",
				price = BigDecimal(1223.99),
				volume = 454,
				comment = "Buy It, on the rise"
			),
			Trade(
				tradeId = "4",
				isin = "US0231351067",
				symbol = "Amazon Inc",
				price = BigDecimal(1330),
				volume = 1200,
				comment = "Buy It"
			)
		).map {
			tradeCache.put(it.tradeId, it)
			it.tradeId
		}.map {
			tradeCache.get(it)
		}.subscribe {
			log.info(it.toString())
		}


	}

}