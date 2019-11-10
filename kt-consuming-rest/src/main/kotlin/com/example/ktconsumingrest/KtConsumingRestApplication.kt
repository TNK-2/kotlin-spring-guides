package com.example.ktconsumingrest

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.client.RestTemplateBuilder



@SpringBootApplication
class KtConsumingRestApplication {

	private val logger = LoggerFactory.getLogger(KtConsumingRestApplication::class.java)

	@Bean
	fun restTemplate(builder: RestTemplateBuilder): RestTemplate {
		return builder.build()
	}

	@Bean
	@Throws(Exception::class)
	fun run(restTemplate: RestTemplate) = CommandLineRunner {
		val quote = restTemplate.getForObject(
				"https://gturnquist-quoters.cfapps.io/api/random",
					Quote::class.java)
		logger.info(quote!!.toString())
	}
}

fun main(args: Array<String>) {
	runApplication<KtConsumingRestApplication>(*args)
}