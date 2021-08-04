package com.saleseaze.api.service

import com.saleseaze.api.entity.SocialAccount
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.model.RegisterSocialAccount
import com.saleseaze.api.repository.SocialAccountRepository
import com.saleseaze.api.utils.KeycloakUtils
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.stereotype.Service

@Service
class SocialAccountService(
    private val userProfileService: UserProfileService,
    private val socialAccountRepository: SocialAccountRepository,
    private val keycloakUtils: KeycloakUtils
) {

    fun fetchRegisteredSocialAccounts(userId: String): List<SocialAccount> {
        val userProfile = userProfileService.getUserProfile(userId)
        if (userProfile.company == null) {
            throw InvalidDataException(
                "User $userId is not associate with " +
                        "any company. Please contact administrator"
            )
        }
        userProfile.company?.id?.let {
            return socialAccountRepository.findAllByCompanyId(
                it
            )
        }
        return emptyList()
    }

    fun registerSocialAccount(
        principal: KeycloakAuthenticationToken,
        registerSocialAccount: RegisterSocialAccount
    ): SocialAccount {
        val userProfile = userProfileService.getUserProfile(
            principal.account.keycloakSecurityContext
                .token.subject
        )
        if (userProfile.company == null) {
            throw InvalidDataException(
                "User ${
                    principal.account.keycloakSecurityContext
                        .token.subject
                } is not associate with " +
                        "any company. Please contact administrator"
            )
        }
        val exitingAccount = socialAccountRepository.findByAccountId(
            registerSocialAccount.id
        )
        if(exitingAccount.isPresent) {
            throw InvalidDataException("Account ID ${registerSocialAccount
                .id} already present")
        }
        val socialAccount = SocialAccount(
            name = registerSocialAccount.name,
            accountId = registerSocialAccount.id,
            companyId = userProfile.company!!.id!!,
            accessToken = registerSocialAccount.accessToken,
            graphDomain = registerSocialAccount.graphDomain,
            signedRequest = registerSocialAccount.signedRequest,
            userID = registerSocialAccount.userID,
            createdBy = keycloakUtils.getCurrentUserName(),
            modifiedBy = keycloakUtils.getCurrentUserName()
        )
        return socialAccountRepository.save(socialAccount)
    }
}