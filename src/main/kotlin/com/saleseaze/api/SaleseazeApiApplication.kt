package com.saleseaze.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.retry.annotation.EnableRetry

@SpringBootApplication
@ConfigurationPropertiesScan("com.saleseaze.api.config")
@EnableRetry
class SaleseazeApiApplication

fun main(args: Array<String>) {
    runApplication<SaleseazeApiApplication>(*args)
}
