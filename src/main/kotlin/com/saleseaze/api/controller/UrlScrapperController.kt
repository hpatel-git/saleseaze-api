package com.saleseaze.api.controller

import com.saleseaze.api.service.UrlScrapperService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/tools")
class UrlScrapperController(val urlScrapperService: UrlScrapperService) {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @GetMapping("scrap_urls")
    fun scrapeUrl(
        @RequestParam("url") url: String
    ) = urlScrapperService.scrapeUrl(
        url
    )
}