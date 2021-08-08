package com.saleseaze.api.service

import com.saleseaze.api.client.FacebookClient
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

    fun publishPost(publishPostRequest: PublishPostRequest): List<PublishPost> {
        val pageIds = facebookPageRepository.findAllById(
             publishPostRequest
                .pageIds
        )
        pageIds.forEach {
            if (!commonService.isUserAuthorized(it.companyId)) {
                throw InvalidDataException("You are not authorized to publish")
            }
        }
        return pageIds.map {
            var publishPost = PublishPost(
                accountId = it.accountId,
                companyId = it.companyId,
                message = publishPostRequest.message,
                link = publishPostRequest.link,
                ogImage= publishPostRequest.ogImage,
                ogTitle= publishPostRequest.ogTitle,
                ogDescription= publishPostRequest.ogDescription,
                ogSiteName = publishPostRequest.ogSiteName,
                pageId= it.id!!,
                createdBy = commonService.getCurrentUserName(),
                createdDate = LocalDateTime.now(),
                modifiedBy = commonService.getCurrentUserName(),
                modifiedDate = LocalDateTime.now()
            )
            publishPost = publishPostRepository.save(publishPost)
            try {
                val response = facebookClient.publishFeed(
                    it.accessToken,
                    it.id,
                    publishPostRequest.message,
                    publishPostRequest.link
                )
                publishPost.responseStatus = response.statusCode.reasonPhrase
                if(response.statusCode == HttpStatus.OK) {
                    publishPost.status = PublishStatus.SUCCESS
                    publishPost.response = response.body
                } else {
                    publishPost.status = PublishStatus.ERROR
                    publishPost.response = response.body
                }
            }catch (e:Exception) {
                publishPost.status = PublishStatus.ERROR
                publishPost.response = e.message
            }
            publishPost = publishPostRepository.save(publishPost)
            publishPost
        }
    }

    fun findAllByCompanyId(pageRequest: PageRequest): Page<PublishPost> {
        val currentCompany = commonService.getCurrentUserCompanyId()
        return publishPostRepository.findAllByCompanyId(currentCompany, pageRequest)
    }

}