package com.saleseaze.api.repository

import com.saleseaze.api.entity.Company
import org.springframework.data.repository.CrudRepository
import java.util.Optional
import java.util.UUID

interface CompanyRepository : CrudRepository<Company, UUID> {
    fun findByCompanyName(companyName: String): Optional<Company>
}