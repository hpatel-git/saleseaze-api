package com.saleseaze.api.model

import com.saleseaze.api.entity.Company
import com.saleseaze.api.entity.UserCompanyMapping
import org.keycloak.representations.idm.UserRepresentation

data class UserProfileResponse(
    val userProfile: UserRepresentation,
    var company: Company? = null,
    var userCompanyMappings: List<UserCompanyMapping>? = null
)
