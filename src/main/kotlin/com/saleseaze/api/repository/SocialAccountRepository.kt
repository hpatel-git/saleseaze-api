package com.saleseaze.api.repository

import com.saleseaze.api.entity.SocialAccount
import org.springframework.data.repository.CrudRepository
import java.util.Optional

interface SocialAccountRepository: CrudRepository<SocialAccount, String> {
    fun findAllByCompanyId(companyId: String): List<SocialAccount>
    fun findByAccountId(accountId: String): Optional<SocialAccount>
}