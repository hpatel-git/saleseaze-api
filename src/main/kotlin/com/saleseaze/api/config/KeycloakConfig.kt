package com.saleseaze.api.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
data class KeycloakConfig(
    @Value("\${keycloak.auth-server-url}")
    val authServerUrl: String,
    @Value("\${keycloak.realm}")
    val realm: String,
    @Value("\${keycloak.resource}")
    val resource: String,
    @Value("\${keycloak.ssl-required}")
    val sslRequired: String,
    @Value("\${keycloak.principal-attribute}")
    val principalAttribute: String,
    @Value("\${keycloak.use-resource-role-mappings}")
    val useResourceRoleMappings: Boolean
)