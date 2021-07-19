package com.saleseaze.api.controller

import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
@RequestMapping("/api/profiles")
class UserProfileController {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @GetMapping("/me")
    fun getUserExtra(principal: Principal): String? {
        return principal.name
    }
}