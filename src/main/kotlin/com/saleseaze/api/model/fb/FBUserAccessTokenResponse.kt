package com.saleseaze.api.model.fb

import com.fasterxml.jackson.annotation.JsonProperty


data class FBUserAccessTokenResponse(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("token_type")
    val tokenType: String,
    @JsonProperty("expires_in")
    val expiresIn: Long
)

