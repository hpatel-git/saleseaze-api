package com.saleseaze.api.model

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RegisterSocialAccount(
    @field:NotNull
    @field:Size(min = 2, message = "Account Name is required")
    val name: String,
    @field:NotNull
    @field:Size(min = 2, message = "ID is required")
    val id: String,
    @field:NotNull
    @field:Size(min = 2, message = "Access Token is required")
    val accessToken: String,
    @field:NotNull
    @field:Size(min = 2, message = "User ID is required")
    val userID: String,
    @field:NotNull
    @field:Size(min = 2, message = "Signed Request is required")
    val signedRequest: String,
    @field:NotNull
    @field:Size(min = 2, message = "Graph Domain is required")
    val graphDomain: String
)