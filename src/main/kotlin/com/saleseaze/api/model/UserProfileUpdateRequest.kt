package com.saleseaze.api.model

data class UserProfileUpdateRequest(
    var userId: String?,
    val userName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val city: String,
    val country: String,
    val postalCode: String
)
