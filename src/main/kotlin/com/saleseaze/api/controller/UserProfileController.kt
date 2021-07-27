package com.saleseaze.api.controller

import com.saleseaze.api.model.UserProfileUpdateRequest
import com.saleseaze.api.service.UserProfileService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/profiles")
class UserProfileController(
    private val userProfileService:
    UserProfileService
) {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @GetMapping("/me")
    fun getMyProfile(
        principal: KeycloakAuthenticationToken
    ): UserRepresentation {
        return userProfileService.getUserProfile(
            principal.account.keycloakSecurityContext
                .token.subject
        )
    }

    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @PutMapping("/me")
    fun updateMyProfile(
            principal: KeycloakAuthenticationToken,
        @RequestBody userProfileUpdateRequest: UserProfileUpdateRequest
    ) {
        userProfileUpdateRequest.userId = principal
            .account
            .keycloakSecurityContext
            .token
            .subject
        userProfileService.updateUserProfile(userProfileUpdateRequest)
    }
}