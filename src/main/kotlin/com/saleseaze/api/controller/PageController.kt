package com.saleseaze.api.controller

import com.saleseaze.api.service.FacebookPageService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/accounts/{accountId}/pages")
class PageController(
    private val facebookPageService: FacebookPageService
) {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @GetMapping
    fun fetchRegisteredSocialAccounts(
        @PathVariable("accountId") accountId: String
    ) = facebookPageService.fetchPagesByAccountId(
        accountId
    )

}