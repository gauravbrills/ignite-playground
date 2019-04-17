package gauravbrills.ignite

import org.apache.ignite.Ignite
import org.apache.ignite.lang.IgniteCallable
import org.apache.ignite.lang.IgniteClosure
import org.springframework.stereotype.Component

@Component
class TaskRunner(val ignite: Ignite) {

	fun <T> executeTask(task: IgniteCallable<T>): Collection<T> {
		return ignite.compute().broadcast(task)
	}

	fun <E, R> executeTask(task: IgniteClosure<E, R>, args: E): Collection<R> {
		return ignite.compute().broadcast(task, args)
	}
}