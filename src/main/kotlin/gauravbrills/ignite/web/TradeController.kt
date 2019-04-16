package gauravbrills.ignite.web

import gauravbrills.ignite.cache.Trade
import gauravbrills.ignite.cache.TradeCacheRespository
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/trades")
class TradeController(val tradeRepo: TradeCacheRespository) {

	@GetMapping("/findByIsin", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun findByIsin(@RequestParam isin: String): Mono<Collection<Trade>> {
		return Mono.fromCallable { tradeRepo.findByIsin(isin) }
	}

	@GetMapping("/shouldSell", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun shouldSell(): Mono<Collection<Trade>> {
		return Mono.fromCallable { tradeRepo.findWhereInstructionToSell() }
	}
}