package com.saleseaze.api.model

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UserCompanyDetails(
    @field:NotNull
    @field:Size(min = 2, message = "Invalid Company Name")
    val companyName: String,
    @field:NotNull
    @field:Size(min = 2, message = "Invalid Address")
    val address: String,
    @field:Size(min = 2, message = "Invalid City")
    val city: String,
    @field:Size(min = 2, message = "Invalid Country")
    val country: String,
    @field:Size(min = 2, message = "Invalid Postal Code")
    val postalCode: String
)
