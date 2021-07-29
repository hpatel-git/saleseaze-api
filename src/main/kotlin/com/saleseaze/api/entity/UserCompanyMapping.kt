package com.saleseaze.api.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document
data class UserCompanyMapping(
    @Id
    val id: String? = null,
    @Indexed
    val companyId: String,
    @Indexed
    val userId: String,
    val userRole: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    val modifiedDate: LocalDateTime = LocalDateTime.now(),
    val modifiedBy: String
)
