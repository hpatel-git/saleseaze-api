package com.saleseaze.api.repository

import com.saleseaze.api.entity.PublishPost
import org.springframework.data.repository.CrudRepository

interface PublishPostRepository: CrudRepository<PublishPost, String> {
    fun findAllByAccountId(accountId: String): List<PublishPost>
    fun findAllByCompanyId(companyId: String): List<PublishPost>
}