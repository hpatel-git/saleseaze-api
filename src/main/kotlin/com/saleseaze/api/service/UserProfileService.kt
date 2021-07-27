package com.saleseaze.api.service

import com.saleseaze.api.config.KeycloakConfig
import com.saleseaze.api.exception.InvalidUserException
import com.saleseaze.api.model.UserProfileUpdateRequest
import org.keycloak.admin.client.Keycloak
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    val keycloakAdmin: Keycloak,
    val keycloakConfig: KeycloakConfig
) {

    fun getUserProfile(userId: String): UserRepresentation {
        return keycloakAdmin
            .realms()
            .realm(keycloakConfig.realm)
            .users()
            .get(userId).toRepresentation()
            ?: throw InvalidUserException(
                "User $userId does not exists"
            )
    }

    fun updateUserProfile(updateUserReq: UserProfileUpdateRequest) {
        val userResource = keycloakAdmin
            .realms()
            .realm(keycloakConfig.realm)
            .users()
            .get(updateUserReq.userId)
            ?: throw InvalidUserException(
                "User ${updateUserReq.userId} does not exists"
            )
        val userPresentation = userResource.toRepresentation()
        if (!userPresentation.isEnabled) {
            throw InvalidUserException(
                "User ${updateUserReq.userId} is not active"
            )
        }
        userPresentation.firstName = updateUserReq.firstName
        userPresentation.lastName = updateUserReq.lastName
        userPresentation.email = updateUserReq.email
        userPresentation.singleAttribute("city", updateUserReq.city)
        userPresentation.singleAttribute("country", updateUserReq.country)
        userPresentation.singleAttribute("postalCode", updateUserReq.postalCode)
        userPresentation.isEmailVerified = false
        userResource.update(userPresentation)
    }
}