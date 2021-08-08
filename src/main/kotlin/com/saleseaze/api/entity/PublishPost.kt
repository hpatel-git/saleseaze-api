package com.saleseaze.api.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

enum class PublishStatus {
    IN_PROCESS,
    SUCCESS,
    ERROR,
    RETRY
}

@Document("publish_post")
data class PublishPost(
    val id: String? = null,
    val companyId: String,
    val message: String,
    val link: String?,
    val ogImage: String?,
    val ogTitle: String?,
    val ogDescription: String?,
    val ogSiteName: String?,
    val publishDetails: List<PublishDetails>,
    var status: PublishStatus = PublishStatus.IN_PROCESS,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var modifiedBy: String
)

class PublishDetails(
    val pageId: String,
    val accountId: String,
    val pageName: String,
    @JsonIgnore
    val accessToken: String,
    var response: Any? = null,
    var responseStatus: String? = null,
    var status: PublishStatus = PublishStatus.IN_PROCESS,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var modifiedBy: String
)
