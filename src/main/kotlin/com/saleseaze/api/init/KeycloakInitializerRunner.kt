package com.saleseaze.api.init

import com.saleseaze.api.utils.ApplicationRoles
import mu.KLogging
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
class KeycloakInitializerRunner {
    companion object : KLogging()

    private val COMPANY_SERVICE_REALM_NAME = "company-services"
    private val SALESEAZE_APP_CLIENT_ID = "saleseaze-app"
    private val SALESEAZE_APP_ROLES: List<String> = listOf(
        ApplicationRoles.SALESEAZE_USER,
        ApplicationRoles.SALESEAZE_MANAGER
    )
    private val SALESEAZE_APP_REDIRECT_URL = "http://localhost:3000/*"
    private val SALESEAZE_APP_USERS: List<UserPass> = arrayListOf(
        UserPass("admin", "admin"),
        UserPass("user", "user")
    )

    @Bean
    fun init(
        @Value("\${keycloak.auth-server-url}")
        keycloakServerUrl: String,
        keycloakAdmin: Keycloak
    ) = CommandLineRunner {
        logger.info("Initializing '${COMPANY_SERVICE_REALM_NAME}' realm in Keycloak ...")
        val representationOptional: Optional<RealmRepresentation> =
            keycloakAdmin.realms().findAll().stream()
                .filter { r -> r.realm.equals(COMPANY_SERVICE_REALM_NAME) }
                .findAny()
        if (!representationOptional.isPresent) {
            val realmRepresentation = RealmRepresentation()
            realmRepresentation.realm = COMPANY_SERVICE_REALM_NAME
            realmRepresentation.isEnabled = true
            realmRepresentation.isRegistrationAllowed = true

            // Client

            // Client
            val clientRepresentation = ClientRepresentation()
            clientRepresentation.clientId = SALESEAZE_APP_CLIENT_ID
            clientRepresentation.isDirectAccessGrantsEnabled = true
            clientRepresentation.setDefaultRoles(
                arrayOf(
                    SALESEAZE_APP_ROLES[0]
                )
            )
            clientRepresentation.isPublicClient = true
            clientRepresentation.redirectUris = listOf(SALESEAZE_APP_REDIRECT_URL)
            realmRepresentation.clients = listOf(clientRepresentation)

            // Users
            // Users
            val userRepresentations = SALESEAZE_APP_USERS.map {
                val clientRoles: MutableMap<String, List<String>> =
                    HashMap()
                if ("admin" == it.userName) {
                    clientRoles[SALESEAZE_APP_CLIENT_ID] = SALESEAZE_APP_ROLES
                } else {
                    clientRoles[SALESEAZE_APP_CLIENT_ID] = listOf(
                        SALESEAZE_APP_ROLES[0]
                    )
                }
                val credentialRepresentation = CredentialRepresentation()
                credentialRepresentation.type =
                    CredentialRepresentation.PASSWORD
                credentialRepresentation.value = it.password

                // User
                val userRepresentation = UserRepresentation()
                userRepresentation.username = it.userName
                userRepresentation.isEnabled = true
                userRepresentation.credentials =
                    listOf(credentialRepresentation)
                userRepresentation.clientRoles = clientRoles
                userRepresentation
            }.toList()
            realmRepresentation.users = userRepresentations
            keycloakAdmin.realms().create(realmRepresentation)

        } else {
            logger.info(
                "Realm '{}' already pre-configured",
                COMPANY_SERVICE_REALM_NAME
            )
        }

        val admin = SALESEAZE_APP_USERS[0]
        logger.info("Testing getting token for '{}' ...", admin.userName)

        val keycloakMovieApp =
            KeycloakBuilder.builder().serverUrl(keycloakServerUrl)
                .realm(COMPANY_SERVICE_REALM_NAME).username(admin.userName)
                .password(admin.password)
                .clientId(SALESEAZE_APP_CLIENT_ID).build()

        logger.info(
            "'{}' token: {}",
            admin.userName,
            keycloakMovieApp.tokenManager().grantToken().token
        )
        logger.info(
            "'{}' initialization completed successfully!",
            COMPANY_SERVICE_REALM_NAME
        )
    }
}

data class UserPass(val userName: String, val password: String)