package gauravbrills.ignite.web

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/cache")
class CacheController {

	@GetMapping("/",produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun cache(): Mono<String> {
		return Mono.fromCallable { "Cache" }
	}
}