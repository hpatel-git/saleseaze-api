package com.saleseaze.api.repository

import com.saleseaze.api.entity.UserCompanyMapping
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface UserCompanyMappingRepository :
    CrudRepository<UserCompanyMapping, String> {
    fun findAllByCompanyId(companyId: String): List<UserCompanyMapping>
    fun findAllByCompanyIdAndUserId(companyId: String, userId: String): List<UserCompanyMapping>
    fun findAllByUserId(userId: String): List<UserCompanyMapping>
}