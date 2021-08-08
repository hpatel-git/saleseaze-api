package com.saleseaze.api.model

data class PublishPostRequest(
    val message: String,
    val link: String?,
    var publishTime: String,
    var pageIds: List<String>
)
