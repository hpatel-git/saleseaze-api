package com.saleseaze.api.model.fb

import com.fasterxml.jackson.annotation.JsonProperty

data class FBPostResponse(
    @JsonProperty("id")
    val id: String
)
