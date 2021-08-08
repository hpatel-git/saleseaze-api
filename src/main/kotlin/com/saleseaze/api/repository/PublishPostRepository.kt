package com.saleseaze.api.repository

import com.saleseaze.api.entity.PublishPost
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.repository.PagingAndSortingRepository

interface PublishPostRepository :
    PagingAndSortingRepository<PublishPost, String> {
    fun findAllByAccountId(accountId: String): List<PublishPost>
    fun findAllByCompanyId(companyId: String, pageRequest: PageRequest):
            Page<PublishPost>
}