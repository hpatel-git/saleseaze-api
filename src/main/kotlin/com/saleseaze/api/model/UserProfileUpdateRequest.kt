package com.saleseaze.api.model

import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserProfileUpdateRequest(
    var userId: String?,
    @field:NotNull
    @field:Size(min= 2, message = "Invalid User Name")
    val userName: String,
    @field:NotNull
    @field:Size(min= 2, message = "Invalid First Name")
    val firstName: String,
    @field:NotNull
    @field:Size(min= 2, message = "Invalid Last Name")
    val lastName: String,
    @field:NotNull
    @field:Size(min= 2, message = "Invalid Email Name")
    val email: String,
    val city: String,
    val country: String,
    val postalCode: String
)
