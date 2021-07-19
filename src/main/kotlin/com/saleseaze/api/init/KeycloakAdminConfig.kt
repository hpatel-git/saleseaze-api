package com.saleseaze.api.init

import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class KeycloakAdminConfig(
    @Value("\${keycloak.auth-server-url}")
    private val keycloakServerUrl: String,
    @Value("\${keycloak-admin.user-name}")
    private val keyCloakAdminUserName: String,
    @Value("\${keycloak-admin.pwd}")
    private val keyCloakAdminPwd: String,
    @Value("\${keycloak-admin.client-id}")
    private val adminClientId: String,
    @Value("\${keycloak-admin.realm}")
    private val adminRealm: String
) {

    @Bean
    fun keycloakAdmin(): Keycloak {
        return KeycloakBuilder.builder()
            .serverUrl(keycloakServerUrl)
            .realm(adminRealm)
            .username(keyCloakAdminUserName)
            .password(keyCloakAdminPwd)
            .clientId(adminClientId)
            .build()
    }

}