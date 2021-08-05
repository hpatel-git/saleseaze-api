package com.saleseaze.api.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.saleseaze.api.model.fb.FBPageCategory
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document("facebook_page")
data class FacebookPage(
    val id: String? = null,
    val accountId: String,
    val companyId: String,
    @JsonIgnore
    var accessToken: String,
    var category: String,
    var name: String,
    var tasks: List<String>,
    var categoryList: List<FBPageCategory>,
    val createdDate: LocalDateTime = LocalDateTime.now(),
    val createdBy: String,
    var modifiedDate: LocalDateTime = LocalDateTime.now(),
    var modifiedBy: String
)

