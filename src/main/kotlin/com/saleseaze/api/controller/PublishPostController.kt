package com.saleseaze.api.controller

import com.saleseaze.api.model.PublishPostRequest
import com.saleseaze.api.service.PublishPostService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
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
        @RequestParam("size", defaultValue = "10") size: Int,
        @RequestParam("page", defaultValue = "0") page: Int,
        @RequestParam(
            "direction",
            defaultValue = "ASC"
        ) direction: Sort.Direction,
        @RequestParam(
            "direction",
            defaultValue = "modifiedDate"
        ) properties: List<String>

    ) = publishPostService
        .findAllByCompanyId(
            PageRequest.of(
                page, size, Sort.by(
                    direction, *properties
                        .toTypedArray()
                )
            )
        )

}