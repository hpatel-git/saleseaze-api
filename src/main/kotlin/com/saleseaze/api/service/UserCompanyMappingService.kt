package com.saleseaze.api.service

import com.saleseaze.api.entity.UserCompanyMapping
import com.saleseaze.api.repository.UserCompanyMappingRepository
import com.saleseaze.api.utils.KeycloakUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.UUID

@Service
class UserCompanyMappingService(
    private val userCompanyMappingRepository: UserCompanyMappingRepository,
    private val keycloakUtils: KeycloakUtils
) {
    fun findAllByCompanyIdAndUserId(
        companyId: String,
        userId: String
    ): List<UserCompanyMapping> {
        return userCompanyMappingRepository
            .findAllByCompanyIdAndUserId(
                companyId, userId
            )
    }

    fun createUserCompanyMapping(
        userId: String,
        companyId: String,
        userRole: String
    ): UserCompanyMapping {
        val userCompanyMapping = UserCompanyMapping(
            userId = userId,
            companyId = companyId,
            userRole = userRole,
            createdBy = keycloakUtils.getCurrentUserName(),
            createdDate = LocalDateTime.now(),
            modifiedBy = keycloakUtils.getCurrentUserName(),
            modifiedDate = LocalDateTime.now()
        )
        return userCompanyMappingRepository.save(userCompanyMapping)
    }
}