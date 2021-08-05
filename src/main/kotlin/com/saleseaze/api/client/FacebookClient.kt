package com.saleseaze.api.client

import com.saleseaze.api.config.FacebookConfig
import com.saleseaze.api.model.fb.FBPageResponse
import com.saleseaze.api.model.fb.FBUserAccessTokenResponse
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class FacebookClient(
    val restTemplate: RestTemplate,
    val facebookConfig: FacebookConfig
) {
    fun generateLongLivedUserAccessToken(accessToken: String): ResponseEntity<FBUserAccessTokenResponse> {
        val baseUrl =
            "${facebookConfig.baseUrl}/${
                facebookConfig
                    .apiVersion
            }/oauth/access_token?grant_type=fb_exchange_token" +
                    "&client_id=${
                        facebookConfig
                            .clientId
                    }&client_secret=${
                        facebookConfig
                            .clientSecret
                    }&fb_exchange_token" +
                    "=$accessToken"
        return restTemplate.getForEntity(
            baseUrl,
            FBUserAccessTokenResponse::class.java
        )

    }

    fun generateLongLivedPageAccessToken(
        longLivedUserAccessToken: String,
        userId: String
    ): ResponseEntity<FBPageResponse> {
        val baseUrl =
            "${facebookConfig.baseUrl}/${
                facebookConfig
                    .apiVersion
            }/$userId/accounts?access_token" +
                    "=$longLivedUserAccessToken"
        return restTemplate.getForEntity(
            baseUrl,
            FBPageResponse::class.java
        )
    }
}