package com.saleseaze.api.service

import com.saleseaze.api.entity.Company
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.model.UserCompanyDetails
import com.saleseaze.api.repository.CompanyRepository
import com.saleseaze.api.utils.KeycloakUtils
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.Optional
import java.util.UUID

@Service
class CompanyService(
    private val companyRepository: CompanyRepository,
    private val keycloakUtils: KeycloakUtils
) {
    fun findByCompanyName(companyName: String): Optional<Company> {
        return companyRepository.findByCompanyName(companyName)
    }

    fun createCompany(userCompanyDetails: UserCompanyDetails): Company {
        findByCompanyName(userCompanyDetails.companyName).orElseThrow {
            InvalidDataException(
                "Company Name ${userCompanyDetails.companyName} " +
                        " already exists"
            )
        }
        val company = Company(
            companyName = userCompanyDetails.companyName,
            address = userCompanyDetails.address,
            city = userCompanyDetails.city,
            country = userCompanyDetails.country,
            postalCode = userCompanyDetails.postalCode,
            createdBy = keycloakUtils.getCurrentUserName(),
            createdDate = LocalDateTime.now(),
            modifiedBy = keycloakUtils.getCurrentUserName(),
            modifiedDate = LocalDateTime.now()
        )
        return companyRepository.save(company)
    }

    fun updateCompany(
        companyId: UUID,
        userCompanyDetails: UserCompanyDetails
    ): Company {
        val existingCompany = companyRepository.findById(companyId)
            .orElseThrow {
                InvalidDataException(
                    "Company ID $companyId does not exists"
                )
            }
        existingCompany.address = userCompanyDetails.address
        existingCompany.city = userCompanyDetails.city
        existingCompany.country = userCompanyDetails.country
        existingCompany.postalCode = userCompanyDetails.postalCode
        existingCompany.modifiedBy = keycloakUtils.getCurrentUserName()
        existingCompany.modifiedDate = LocalDateTime.now()
        return companyRepository.save(existingCompany)
    }
}