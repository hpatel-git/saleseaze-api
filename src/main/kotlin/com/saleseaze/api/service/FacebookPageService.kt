package com.saleseaze.api.service

import com.saleseaze.api.client.FacebookClient
import com.saleseaze.api.entity.FacebookPage
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.repository.FacebookPageRepository
import com.saleseaze.api.utils.KeycloakUtils
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class FacebookPageService(
    private val facebookPageRepository: FacebookPageRepository,
    private val facebookClient: FacebookClient,
    private val keycloakUtils: KeycloakUtils
) {

    fun syncFacebookPages(
        longLivedUserAccessToken: String,
        userId: String,
        companyId: String,
        accountId: String
    ) {
        val pageResponse = facebookClient.generateLongLivedPageAccessToken(
            longLivedUserAccessToken,
            userId
        )
        if (pageResponse.statusCode == HttpStatus.OK) {
            val fbPageResponse = pageResponse.body
            fbPageResponse?.data?.let { pageDetails ->
                pageDetails.forEach { pageDetail ->
                    val existingPage = facebookPageRepository
                        .findById(pageDetail.id)
                    if (existingPage.isPresent) {
                        val facebookExistingPage = existingPage.get()
                        facebookExistingPage.accessToken =
                            pageDetail.accessToken
                        facebookExistingPage.category = pageDetail.category
                        facebookExistingPage.name = pageDetail.name
                        facebookExistingPage.tasks = pageDetail.tasks
                        facebookExistingPage.categoryList = pageDetail
                            .categoryList
                        facebookExistingPage.modifiedBy =
                            keycloakUtils.getCurrentUserName()
                        facebookExistingPage.modifiedDate = LocalDateTime.now()
                        facebookPageRepository.save(facebookExistingPage)

                    } else {
                        val facebookPage = FacebookPage(
                            id = pageDetail.id,
                            accountId = accountId,
                            companyId = companyId,
                            accessToken = pageDetail.accessToken,
                            category = pageDetail.category,
                            name = pageDetail.name,
                            tasks = pageDetail.tasks,
                            categoryList = pageDetail.categoryList,
                            createdBy = keycloakUtils.getCurrentUserName(),
                            modifiedBy = keycloakUtils.getCurrentUserName()
                        )
                        facebookPageRepository.save(facebookPage)
                    }
                }
            }
        } else {
            throw InvalidDataException(
                "Facebook Page sync failed with status" +
                        " code ${pageResponse.statusCode.reasonPhrase}"
            )
        }
    }
}