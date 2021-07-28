package com.saleseaze.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan("com.saleseaze.api.config")
class SaleseazeApiApplication

fun main(args: Array<String>) {
    runApplication<SaleseazeApiApplication>(*args)
}
