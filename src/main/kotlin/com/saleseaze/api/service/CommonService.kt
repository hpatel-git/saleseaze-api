package com.saleseaze.api.service

import com.saleseaze.api.config.KeycloakConfig
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.utils.ApplicationConstants
import com.saleseaze.api.utils.KeycloakUtils
import org.keycloak.admin.client.Keycloak
import org.springframework.stereotype.Service

@Service
class CommonService(
    val keycloakAdmin: Keycloak,
    val keycloakConfig: KeycloakConfig,
    val keycloakUtils: KeycloakUtils
) {
    fun getCurrentUserCompanyId(): String {
        val userProfile = keycloakAdmin
            .realms()
            .realm(keycloakConfig.realm)
            .users()
            .get(keycloakUtils.getCurrentUserId()).toRepresentation()
            ?: throw InvalidDataException(
                "User ${keycloakUtils.getCurrentUserId()} does not exists"
            )
        if (userProfile.attributes != null && userProfile.attributes.containsKey
                (ApplicationConstants.USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY)
        ) {
            return userProfile
                .attributes[ApplicationConstants.USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY]?.get(
                0
            ) ?: ""
        }
        return ""
    }

    fun isUserAuthorized(companyId: String): Boolean {
        val keyCloakCompanyId = getCurrentUserCompanyId()
        if (keyCloakCompanyId == companyId) {
            return true
        }
        return false
    }

    fun getCurrentUserName() = keycloakUtils.getCurrentUserName()
}