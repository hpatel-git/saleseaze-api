package com.saleseaze.api.service

import com.saleseaze.api.config.KeycloakConfig
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.model.UserProfileResponse
import com.saleseaze.api.model.UserProfileUpdateRequest
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_ABOUT_ME
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_CITY
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_COUNTRY
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_PHONE_NUMBER
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_POSTAL_CODE
import com.saleseaze.api.utils.ApplicationConstants.USER_EXTRA_ATTRIBUTE_REGISTRATION_COMPLETE
import com.saleseaze.api.utils.ApplicationRoles
import com.saleseaze.api.utils.KeycloakUtils
import org.keycloak.admin.client.Keycloak
import org.springframework.stereotype.Service

@Service
class UserProfileService(
    val keycloakAdmin: Keycloak,
    val keycloakConfig: KeycloakConfig,
    val keycloakUtils: KeycloakUtils,
    val companyService: CompanyService,
    val companyMappingService: UserCompanyMappingService
) {

    fun getUserProfile(userId: String): UserProfileResponse {
        val userProfile = keycloakAdmin
            .realms()
            .realm(keycloakConfig.realm)
            .users()
            .get(userId).toRepresentation()
            ?: throw InvalidDataException(
                "User $userId does not exists"
            )
        val response = UserProfileResponse(
            userProfile = userProfile
        )

        if (userProfile.attributes.containsKey
                (USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY)
        ) {
            val companyId = userProfile
                .attributes[USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY]?.get(0)
            companyId?.let {
                response.company = companyService.findByCompanyId(it)
                    .orElseThrow {
                        InvalidDataException(
                            "Company id $it does not exists.Please check with admin"
                        )
                    }
                response.userCompanyMappings = companyMappingService
                    .findAllByCompanyIdAndUserId(
                        it, userId
                    )
            }
        }
        return response
    }

    fun updateUserProfile(updateUserReq: UserProfileUpdateRequest) {
        val userResource = keycloakAdmin
            .realms()
            .realm(keycloakConfig.realm)
            .users()
            .get(updateUserReq.userId)
            ?: throw InvalidDataException(
                "User ${updateUserReq.userId} does not exists"
            )

        val userPresentation = userResource.toRepresentation()
        if (!userPresentation.isEnabled) {
            throw InvalidDataException(
                "User ${updateUserReq.userId} is not active"
            )
        }
        val currentUsersRoles = keycloakUtils.getCurrentUserRoles()

        if (currentUsersRoles
                .contains(ApplicationRoles.SALESEAZE_MANAGER.name)
            && updateUserReq.companyDetails != null
        ) {
            val isRegistrationComplete = userPresentation.attributes != null
                    && userPresentation
                .attributes.contains(USER_EXTRA_ATTRIBUTE_REGISTRATION_COMPLETE)
                    && userPresentation
                .attributes.contains(USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY)
            if (!isRegistrationComplete) {
                // Create Company and assigned to user
                val createdCompany =
                    companyService.createCompany(updateUserReq.companyDetails)
                companyMappingService.createUserCompanyMapping(
                    updateUserReq.userId!!,
                    createdCompany.id!!,
                    ApplicationRoles.SALESEAZE_MANAGER.name
                )
                userPresentation.singleAttribute(
                    USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY, createdCompany
                        .id.toString()
                )
                userPresentation.singleAttribute(
                    USER_EXTRA_ATTRIBUTE_REGISTRATION_COMPLETE, "true"
                )
            } else {
                // Update company details
                val companyIdList: MutableList<String>? = userPresentation
                    .attributes[USER_EXTRA_ATTRIBUTE_ASSIGNED_COMPANY]
                companyIdList?.let {
                    companyService.updateCompany(
                        companyIdList[0], updateUserReq.companyDetails
                    )
                }
            }
        }
        userPresentation.firstName = updateUserReq.firstName
        userPresentation.lastName = updateUserReq.lastName
        userPresentation.isEmailVerified =
            userPresentation.email != updateUserReq.email
        userPresentation.email = updateUserReq.email
        userPresentation.singleAttribute(
            USER_EXTRA_ATTRIBUTE_PHONE_NUMBER,
            updateUserReq.phoneNumber
        )
        userPresentation.singleAttribute(
            USER_EXTRA_ATTRIBUTE_CITY, updateUserReq.city
        )
        userPresentation.singleAttribute(
            USER_EXTRA_ATTRIBUTE_COUNTRY, updateUserReq.country
        )
        userPresentation.singleAttribute(
            USER_EXTRA_ATTRIBUTE_POSTAL_CODE, updateUserReq.postalCode
        )
        userPresentation.singleAttribute(
            USER_EXTRA_ATTRIBUTE_ABOUT_ME, updateUserReq.aboutMe
        )
        userResource.update(userPresentation)
    }
}