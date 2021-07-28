package com.saleseaze.api.utils

import org.keycloak.KeycloakPrincipal
import org.keycloak.adapters.AdapterUtils
import org.keycloak.adapters.RefreshableKeycloakSecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class KeycloakUtils {
    fun getCurrentUserName() : String {
        if (SecurityContextHolder.getContext().authentication.principal is
                    KeycloakPrincipal<*>
        ) {
            val principal: KeycloakPrincipal<RefreshableKeycloakSecurityContext> =
                SecurityContextHolder
                    .getContext()
                    .authentication
                    .principal as KeycloakPrincipal<RefreshableKeycloakSecurityContext>

            return AdapterUtils.getPrincipalName(
                principal
                    .keycloakSecurityContext.deployment, principal
                    .keycloakSecurityContext.token
            )
        }
        return ""

    }
    fun getCurrentUserRoles(): MutableSet<String> {
        if (SecurityContextHolder.getContext().authentication.principal is
                    KeycloakPrincipal<*>
        ) {
            val principal: KeycloakPrincipal<RefreshableKeycloakSecurityContext> =
                SecurityContextHolder
                    .getContext()
                    .authentication
                    .principal as KeycloakPrincipal<RefreshableKeycloakSecurityContext>

            return AdapterUtils.getRolesFromSecurityContext(
                principal
                    .keycloakSecurityContext
            )
        }
        return mutableSetOf()
    }
}