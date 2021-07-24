package com.saleseaze.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "saleseaze")
data class SaleseazeConfig(
    val redirectUrl: String,
    val defaultUsers: Map<String, String>
)