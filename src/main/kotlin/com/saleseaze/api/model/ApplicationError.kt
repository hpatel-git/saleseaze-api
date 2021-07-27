package com.saleseaze.api.model


data class ApplicationError(
    val status: Int,
    val message: String,
    val fieldErrors: MutableList<CustomFieldError> = mutableListOf()
) {
    fun addFieldError(objectName: String, field: String, message: String) {
        val error = CustomFieldError(objectName, field, message)
        fieldErrors.add(error)
    }
}

data class CustomFieldError(
    val objectName: String,
    val field: String,
    val message: String
)