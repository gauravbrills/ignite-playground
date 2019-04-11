package gauravbrills.ignite.web

import org.apache.ignite.Ignite
import org.apache.ignite.cache.query.SqlFieldsQuery
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.LinkedList

@RestController
@RequestMapping("/caches")
class CacheController(private val ignite: Ignite) {


	@GetMapping("/", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun cache(): Mono<Collection<String>> {
		return Mono.fromCallable { ignite.cacheNames() }
	}

	@PostMapping("/{cacheName}/query", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun query(@PathVariable cacheName: String, @RequestBody query: String): Mono<LinkedList<Map<String, Any?>>> {
		val data = LinkedList<Map<String, Any?>>();
		val q = ignite.getOrCreateCache<String, Any>(cacheName).withKeepBinary<String, Any>()
			.query(SqlFieldsQuery(query))
		val iter = q.iterator();
		while (iter.hasNext()) {
			val row = HashMap<String, Any?>()
			val next = iter.next()
			for (i in 0..next.size) {
				row.put(q.getFieldName(i), next.get(i));
			}
			data.add(row);
		}
		return Mono.fromCallable { data }
	}
}