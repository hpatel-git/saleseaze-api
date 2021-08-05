package com.saleseaze.api.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document("social_account")
data class SocialAccount(
    @Id
    val id: String? = null,
    val name: String,
    val companyId: String,
    val accountId: String,
    val accessToken: String,
    val longLivedAccessToken: String,
    val userID: String,
    val signedRequest: String,
    val graphDomain: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var modifiedBy: String,
    var isDeleted: Boolean = false
)
