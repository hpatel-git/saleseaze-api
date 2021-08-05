package com.saleseaze.api.repository

import com.saleseaze.api.entity.FacebookPage
import org.springframework.data.repository.CrudRepository

interface FacebookPageRepository: CrudRepository<FacebookPage, String> {
    fun findAllByAccountIdAndCompanyId(accountId: String, companyId: String):
            List<FacebookPage>
}