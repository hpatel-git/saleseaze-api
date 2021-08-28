package com.saleseaze.api.config

import com.cloudinary.Cloudinary
import com.cloudinary.utils.ObjectUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class StorageConfig(
    @Value("\${storage.api-key}")
    val apiKey: String,
    @Value("\${storage.api-secret}")
    val apiSecret: String,
    @Value("\${storage.cloud-name}")
    val cloudName: String
) {
    @Bean
    fun configureCloudinary() = Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", cloudName,
            "api_key", apiKey,
            "api_secret", apiSecret
        )
    )
}