package com.saleseaze.api.repository

import com.saleseaze.api.entity.SocialAccount
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface SocialAccountRepository: CrudRepository<SocialAccount, String> {
    fun findAllByCompanyIdAndIsDeleted(
        companyId: String,isDeleted: Boolean
    ): List<SocialAccount>
    fun findByAccountIdAndIsDeleted(
        accountId: String, isDeleted: Boolean
    ): Optional<SocialAccount>
}