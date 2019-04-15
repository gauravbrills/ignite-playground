package gauravbrills.ignite

import org.apache.ignite.IgniteException
import org.apache.ignite.lifecycle.LifecycleBean
import org.apache.ignite.lifecycle.LifecycleEventType
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantReadWriteLock

private val log = LoggerFactory.getLogger(IgniteLifeCyclebean::class.java)

/*
 * Hook For Ignite Life Cycle
 */
@Component
class IgniteLifeCyclebean(
	var lock: ReentrantReadWriteLock = ReentrantReadWriteLock(),
	var startingCondition: Condition = lock.writeLock().newCondition(),
	var started: AtomicBoolean = AtomicBoolean(false)
) : LifecycleBean {

	@EventListener(ApplicationReadyEvent::class)
	fun onApplicationStart() {
		if (!waitForIgniteToStart(10000)) {
			log.info("Waiting for ingite to start");
		} else {
			log.info("Ignite Started :) ");
		}

		init()
	}

	fun init() {

	}

	private fun waitForIgniteToStart(timeInMillis: Long): Boolean {
		do {
			try {
				lock.writeLock().lock();
				if (started.get()) {
					return true;
				} else {
					if (startingCondition.await(timeInMillis, TimeUnit.MILLISECONDS)) {
						return false;
					}
					if (started.get()) {
						return true;
					}
				}
			} finally {
				lock.writeLock().unlock();
			}
		} while (true)
	}

	override fun onLifecycleEvent(evt: LifecycleEventType?) {
		try {
			if (evt == LifecycleEventType.AFTER_NODE_START) {
				try {
					lock.writeLock().lock();
					started.set(true);
					startingCondition.signalAll();
				} finally {
					lock.writeLock().unlock();
				}
			}
		} catch (e: Exception) {
			throw IgniteException(e)
		}
	}
}