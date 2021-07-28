package com.saleseaze.api.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document
data class Company(
    @Id
    val id: UUID? = null,
    val companyName: String,
    var address: String,
    var city: String,
    var country: String,
    var postalCode: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var modifiedBy: String
)
