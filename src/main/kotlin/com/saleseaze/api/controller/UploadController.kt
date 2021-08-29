package com.saleseaze.api.controller

import com.saleseaze.api.service.FacebookPageService
import com.saleseaze.api.service.UploadService
import com.saleseaze.api.utils.ApplicationConstants
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/uploads")
class UploadController(
    private val uploadService: UploadService
) {
    @Operation(
        security = [SecurityRequirement(
            name = ApplicationConstants.BEARER_KEY_SECURITY_SCHEME
        )]
    )
    @PostMapping("/images", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadImages(
        @RequestPart file: MultipartFile
    ) = uploadService.uploadImage(file)

}