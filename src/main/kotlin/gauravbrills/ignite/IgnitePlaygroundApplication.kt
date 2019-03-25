package gauravbrills.ignite

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.Banner

@SpringBootApplication
open class IgnitePlaygroundApplication

fun main(args: Array<String>) {
	runApplication<IgnitePlaygroundApplication>(*args) {
		setBannerMode(Banner.Mode.OFF)
	}
}
