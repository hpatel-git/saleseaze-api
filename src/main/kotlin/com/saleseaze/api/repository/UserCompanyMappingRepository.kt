package com.saleseaze.api.repository

import com.saleseaze.api.entity.UserCompanyMapping
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserCompanyMappingRepository :
    CrudRepository<UserCompanyMapping, UUID> {
    fun findAllByCompanyId(companyId: UUID): List<UserCompanyMapping>
    fun findAllByUserId(userId: String): List<UserCompanyMapping>
}