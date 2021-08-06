package com.saleseaze.api.service

import com.saleseaze.api.client.FacebookClient
import com.saleseaze.api.entity.SocialAccount
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.model.RegisterSocialAccount
import com.saleseaze.api.repository.SocialAccountRepository
import com.saleseaze.api.utils.ApplicationRoles
import com.saleseaze.api.utils.KeycloakUtils
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class SocialAccountService(
    private val userProfileService: UserProfileService,
    private val socialAccountRepository: SocialAccountRepository,
    private val keycloakUtils: KeycloakUtils,
    private val facebookClient: FacebookClient,
    private val facebookPageService: FacebookPageService
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
            return socialAccountRepository.findAllByCompanyIdAndIsDeleted(
                it, false
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
        val exitingAccount =
            socialAccountRepository.findByAccountIdAndIsDeleted(
                registerSocialAccount.id, false
            )
        if (exitingAccount.isPresent) {
            throw InvalidDataException(
                "Account ID ${
                    registerSocialAccount
                        .id
                } already present"
            )
        }
        val longLivedUserAccessTokenRes = facebookClient
            .generateLongLivedUserAccessToken(registerSocialAccount.accessToken)
        if (longLivedUserAccessTokenRes.statusCode != HttpStatus.OK) {
            throw InvalidDataException(
                "Facebook Graph API is throwing " +
                        "${longLivedUserAccessTokenRes.statusCode.reasonPhrase} " +
                        " "
            )
        }
        val socialAccount = SocialAccount(
            name = registerSocialAccount.name,
            accountId = registerSocialAccount.id,
            companyId = userProfile.company!!.id!!,
            accessToken = registerSocialAccount.accessToken,
            longLivedAccessToken = longLivedUserAccessTokenRes.body!!.accessToken,
            graphDomain = registerSocialAccount.graphDomain,
            signedRequest = registerSocialAccount.signedRequest,
            userID = registerSocialAccount.userID,
            createdBy = keycloakUtils.getCurrentUserName(),
            modifiedBy = keycloakUtils.getCurrentUserName(),
            isDeleted = false
        )

        facebookPageService.syncFacebookPages(
            socialAccount.longLivedAccessToken,
            socialAccount.userID,
            socialAccount.companyId,
            socialAccount.accountId
        )
        return socialAccountRepository.save(socialAccount)
    }

    fun deListSocialAccount(
        id: String
    ) {
        val existingSocialAccount = socialAccountRepository.findById(id)
        if (!existingSocialAccount.isPresent) {
            throw InvalidDataException("ID $id does not present")
        }
        val currentUsersRoles = keycloakUtils.getCurrentUserRoles()

        if (currentUsersRoles
                .contains(ApplicationRoles.SALESEAZE_USER.name)
        ) {
            throw InvalidDataException(
                "You are not authorized to delist " +
                        "social account"
            )
        }
        val socialAccount = existingSocialAccount.get()
        socialAccount.isDeleted = true
        socialAccount.modifiedDate = LocalDateTime.now()
        socialAccount.modifiedBy = keycloakUtils.getCurrentUserName()
        socialAccountRepository.save(socialAccount)
    }

    fun initiateSync(id: String) {
        val socialAccountOptional = socialAccountRepository
            .findByAccountIdAndIsDeleted(id, false)
        if (!socialAccountOptional.isPresent) {
            throw InvalidDataException(
                "Account ID $id not present"
            )
        }
        val socialAccount = socialAccountOptional.get()
        facebookPageService.syncFacebookPages(
            socialAccount.longLivedAccessToken,
            socialAccount.userID,
            socialAccount.companyId,
            socialAccount.accountId
        )
    }
}