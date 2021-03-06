package gauravbrills.ignite.web

import gauravbrills.ignite.cache.Trade
import org.apache.ignite.Ignite
import org.apache.ignite.cache.query.SqlFieldsQuery
import org.apache.ignite.cache.query.TextQuery
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.LinkedList
import javax.cache.Cache
import gauravbrills.ignite.TaskRunner
import org.apache.ignite.cluster.ClusterNode

@RestController
@RequestMapping("/caches")
class CacheController(private val ignite: Ignite, private val taskRunner: TaskRunner) {


	@GetMapping("/", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun cache(): Mono<Collection<String>> {
		return Mono.fromCallable { ignite.cacheNames() }
	}


	// Creates Dynamic Cache with BinarObject

	@PostMapping("/", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun createDynamicCache(schema: List<Map<String, String>>) {

	}

	@PostMapping("/runDistributedTask", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun runDistributedTask() {
		 
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
			for (i in 0..next.size - 1) {
				row.put(q.getFieldName(i), next.get(i));
			}
			data.add(row);
		}
		return Mono.fromCallable { data }
	}

	@GetMapping("/{cacheName}/freeText", produces = [MediaType.APPLICATION_STREAM_JSON_VALUE])
	@ResponseBody
	fun freeText(@PathVariable cacheName: String, @RequestParam q: String): Mono<LinkedList<Map<String, Trade>>> {
		val data = LinkedList<Map<String, Trade>>();
		val cursor = ignite.getOrCreateCache<String, Any>(cacheName).withKeepBinary<String, Any>()
			.query(TextQuery<String, Trade>(Trade::class.java, q))
		val iter = cursor.iterator();
		while (iter.hasNext()) {
			val row = HashMap<String, Trade>()
			val next = iter.next()
			row.put(next.getKey(), next.getValue());
			data.add(row);
		}
		return Mono.fromCallable { data }
	}
}