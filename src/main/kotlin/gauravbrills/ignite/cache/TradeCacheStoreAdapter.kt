package gauravbrills.ignite.cache

import javax.cache.Cache.Entry
import javax.cache.integration.CacheLoaderException
import javax.cache.integration.CacheWriterException
import org.apache.ignite.cache.store.CacheStoreAdapter
import org.slf4j.Logger
import org.slf4j.LoggerFactory

// for write behind
class TradeCacheStoreAdapter : CacheStoreAdapter<String, Trade>() {
	@Override
	@Throws(CacheLoaderException::class)
	override
	fun load(key: String?): Trade? {
		log!!.info("get from Db (read through) ")
		return null
	}

	@Override
	@Throws(CacheWriterException::class)
	override
	fun write(entry: Entry<out String, out Trade>?) {
		log!!.info("get from Db (write through/behind) ")
	}

	@Override
	@Throws(CacheWriterException::class)
	override
	fun delete(key: Any?) {
		log!!.info("delete from DB ")
	}

	companion object {
		private val log = LoggerFactory.getLogger(TradeCacheStoreAdapter::class.java)
	}
}