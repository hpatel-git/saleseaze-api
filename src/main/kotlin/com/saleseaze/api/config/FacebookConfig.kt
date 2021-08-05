package com.saleseaze.api.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "facebook")
data class FacebookConfig(
    val clientId: String,
    val clientSecret: String,
    val apiVersion: String,
    val baseUrl: String
)