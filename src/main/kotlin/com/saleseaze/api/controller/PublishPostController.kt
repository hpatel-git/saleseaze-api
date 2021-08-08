package com.saleseaze.api.controller

import com.saleseaze.api.model.PublishPostRequest
import com.saleseaze.api.service.FacebookPageService
import com.saleseaze.api.service.PublishPostService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/publish_posts")
class PublishPostController(
    private val publishPostService: PublishPostService
) {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @PostMapping
    fun publishPost(
        @RequestBody publishPostRequest: PublishPostRequest
    ) = publishPostService.publishPost(publishPostRequest)

    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @GetMapping
    fun findAllByCompanyId(
    ) = publishPostService.findAllByCompanyId()

}