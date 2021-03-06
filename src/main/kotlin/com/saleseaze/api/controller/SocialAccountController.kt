package com.saleseaze.api.controller

import com.saleseaze.api.model.RegisterSocialAccount
import com.saleseaze.api.service.SocialAccountService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/accounts")
class SocialAccountController(
    private val socialAccountService:
    SocialAccountService
) {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @GetMapping
    fun fetchRegisteredSocialAccounts(
        principal: KeycloakAuthenticationToken
    ) = socialAccountService.fetchRegisteredSocialAccounts(
        principal.account.keycloakSecurityContext
            .token.subject
    )

    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @PostMapping
    fun registerSocialAccount(
        principal: KeycloakAuthenticationToken,
        @Valid @RequestBody userProfileUpdateRequest: RegisterSocialAccount
    ) = socialAccountService.registerSocialAccount(
        principal,
        userProfileUpdateRequest
    )

    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @DeleteMapping("/{id}")
    fun deListSocialAccount(
        @PathVariable("id") id: String
    ) = socialAccountService.deListSocialAccount(
        id
    )

    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @PutMapping("/{id}/sync_pages")
    fun initiateSyncPages(
        @PathVariable("id") id: String
    ) = socialAccountService.initiateSync(
        id
    )

}