package com.saleseaze.api.model.fb

import com.fasterxml.jackson.annotation.JsonProperty

data class FBPageResponse(
    @JsonProperty("data")
    val data: List<FBPageDetails>
)

data class FBPageDetails(
    @JsonProperty("access_token")
    val accessToken: String,
    @JsonProperty("category")
    val category: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("id")
    val id: String,
    @JsonProperty("tasks")
    val tasks: List<String>,
    @JsonProperty("category_list")
    val categoryList: List<FBPageCategory>
)

data class FBPageCategory(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String
)