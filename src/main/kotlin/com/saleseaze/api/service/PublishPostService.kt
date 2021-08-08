package com.saleseaze.api.service

import com.saleseaze.api.client.FacebookClient
import com.saleseaze.api.entity.PublishDetails
import com.saleseaze.api.entity.PublishPost
import com.saleseaze.api.entity.PublishStatus
import com.saleseaze.api.exception.InvalidDataException
import com.saleseaze.api.model.PublishPostRequest
import com.saleseaze.api.repository.FacebookPageRepository
import com.saleseaze.api.repository.PublishPostRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PublishPostService(
    private val facebookPageRepository: FacebookPageRepository,
    private val facebookClient: FacebookClient,
    private val publishPostRepository: PublishPostRepository,
    private val commonService: CommonService
) {
    fun publishPost(publishPostRequest: PublishPostRequest): PublishPost {
        val pageIds = facebookPageRepository.findAllById(
             publishPostRequest
                .pageIds
        )
        pageIds.forEach {
            if (!commonService.isUserAuthorized(it.companyId)) {
                throw InvalidDataException("You are not authorized to publish")
            }
        }
        val createdDate = LocalDateTime.now()
        var publishPost = PublishPost(
            companyId = commonService.getCurrentUserCompanyId(),
            message = publishPostRequest.message,
            link = publishPostRequest.link,
            ogImage= publishPostRequest.ogImage,
            ogTitle= publishPostRequest.ogTitle,
            ogDescription= publishPostRequest.ogDescription,
            ogSiteName = publishPostRequest.ogSiteName,
            publishDetails= pageIds.map { PublishDetails(
                pageId = it.id!!,
                pageName= it.name,
                accessToken = it.accessToken,
                accountId = it.accountId,
                createdBy = commonService.getCurrentUserName(),
                createdDate = createdDate,
                modifiedBy = commonService.getCurrentUserName(),
                modifiedDate = createdDate
            ) },
            createdBy = commonService.getCurrentUserName(),
            createdDate = createdDate,
            modifiedBy = commonService.getCurrentUserName(),
            modifiedDate = createdDate
        )
        publishPost = publishPostRepository.save(publishPost)
        publishPost.publishDetails.forEach {
            try {
                val response = facebookClient.publishFeed(
                    it.accessToken,
                    it.pageId,
                    publishPostRequest.message,
                    publishPostRequest.link
                )
                it.responseStatus = response.statusCode.reasonPhrase
                if(response.statusCode == HttpStatus.OK) {
                    it.status = PublishStatus.SUCCESS
                    it.response = response.body
                } else {
                    it.status = PublishStatus.ERROR
                    it.response = response.body
                }
            }catch (e:Exception) {
                it.status = PublishStatus.ERROR
                it.response = e.message
            }
        }
        if(publishPost.publishDetails.any { it.status != PublishStatus
            .SUCCESS}) {
            publishPost.status = PublishStatus.RETRY
        } else {
            publishPost.status = PublishStatus.SUCCESS
        }
        publishPost.modifiedBy = commonService.getCurrentUserName()
        publishPost.modifiedDate = LocalDateTime.now()
        return publishPostRepository.save(publishPost)
    }

    fun findAllByCompanyId(pageRequest: PageRequest): Page<PublishPost> {
        val currentCompany = commonService.getCurrentUserCompanyId()
        return publishPostRepository.findAllByCompanyId(currentCompany, pageRequest)
    }

}