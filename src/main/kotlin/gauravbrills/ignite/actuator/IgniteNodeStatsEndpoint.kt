package gauravbrills.ignite.actuator

import org.apache.ignite.Ignite
import org.apache.ignite.cluster.ClusterNode
import org.apache.ignite.configuration.CacheConfiguration
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.annotation.Endpoint
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation
import org.springframework.stereotype.Component

@Component
@Endpoint(id = "nodeStats", enableByDefault = true)
class IgniteNodeStatsEndpoint {
	lateinit private @Autowired
	var ignite: Ignite

	@ReadOperation
	fun igniteNodeStats(): Map<String, Any> {
		var result = hashMapOf<String, Any>();

		for (node in ignite.cluster().nodes()) {
			result.put(node.consistentId().toString(), populateNodeDetails(node));
		}
		return result
	}

	@Suppress("UNCHECKED_CAST")
	fun populateNodeDetails(node: ClusterNode): Map<String, Any> {
		var detailsMap = hashMapOf<String, Any>();
		var metrics = node.metrics()
		detailsMap.put("node.consistentId", node.consistentId());
		detailsMap.put("node.addresses", node.addresses());
		detailsMap.put("node.isLocal", node.isLocal);
		detailsMap.put("node.hostnames", node.hostNames());
		detailsMap.put("node.id", node.id());

		var metricsMap = hashMapOf<String, Any>();
		metricsMap.put("cpuLoad", metrics.currentCpuLoad);
		metricsMap.put("heapUsed", metrics.heapMemoryUsed);
		metricsMap.put("heapCommitted", metrics.heapMemoryCommitted);
		metricsMap.put("heapMax", metrics.heapMemoryMaximum);
		metricsMap.put("heapTotal", metrics.heapMemoryTotal);
		metricsMap.put("nonHeapUsed", metrics.nonHeapMemoryUsed);
		metricsMap.put("nonHeapTotal", metrics.nonHeapMemoryTotal);
		metricsMap.put("nonHeapMax", metrics.nonHeapMemoryMaximum);
		metricsMap.put("totalCpu", metrics.totalCpus);
		metricsMap.put("nonHeapInitialized", metrics.nonHeapMemoryInitialized);
		metricsMap.put("currentActiveJobs", metrics.currentActiveJobs);
		detailsMap.put("metrics", metricsMap);

		return detailsMap
	}
}