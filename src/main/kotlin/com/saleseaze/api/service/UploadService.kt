package com.saleseaze.api.service

import com.cloudinary.Cloudinary
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class UploadService(val cloudinary: Cloudinary) {

    fun uploadImage(file: MultipartFile): MutableMap<Any?, Any?>? {
        val options = mapOf<String, String>("filename" to file.name)
        return cloudinary.uploader().upload(file.bytes,options)
    }
}