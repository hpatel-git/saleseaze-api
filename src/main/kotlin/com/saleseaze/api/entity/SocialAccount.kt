package com.saleseaze.api.entity

import com.fasterxml.jackson.annotation.JsonIgnore
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
    @JsonIgnore
    val accessToken: String,
    @JsonIgnore
    val longLivedAccessToken: String,
    val userID: String,
    @JsonIgnore
    val signedRequest: String,
    val graphDomain: String,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var modifiedBy: String,
    var isDeleted: Boolean = false
)
