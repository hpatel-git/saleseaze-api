package com.saleseaze.api.init

import com.saleseaze.api.config.KeycloakConfig
import com.saleseaze.api.config.SaleseazeConfig
import com.saleseaze.api.utils.ApplicationRoles
import mu.KLogging
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.representations.idm.ClientRepresentation
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.RealmRepresentation
import org.keycloak.representations.idm.RoleRepresentation
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.*


@Configuration
class KeycloakInitializerRunner(
    val keycloakConfig: KeycloakConfig,
    val saleseazeConfig: SaleseazeConfig,
    val keycloakAdmin: Keycloak
) {
    companion object : KLogging()

    @Bean
    fun init(

    ) = CommandLineRunner {
        logger.info("Initializing '${keycloakConfig.realm}' realm in Keycloak ...")
        val representationOptional: Optional<RealmRepresentation> =
            keycloakAdmin.realms().findAll().stream()
                .filter { r -> r.realm.equals(keycloakConfig.realm) }
                .findAny()
        if (!representationOptional.isPresent) {
            val realmRepresentation = RealmRepresentation()
            realmRepresentation.realm = keycloakConfig.realm
            realmRepresentation.isEnabled = true
            realmRepresentation.isRegistrationAllowed = true
            val defaultRole = RoleRepresentation()
            defaultRole.name = ApplicationRoles.SALESEAZE_USER.name
            defaultRole.isComposite = false
            defaultRole.clientRole = true
            realmRepresentation.defaultRole = defaultRole

            val clientRepresentation = ClientRepresentation()
            clientRepresentation.clientId = keycloakConfig.resource
            clientRepresentation.isDirectAccessGrantsEnabled = true
            clientRepresentation.isPublicClient = true
            clientRepresentation.redirectUris =
                listOf(saleseazeConfig.redirectUrl)
            realmRepresentation.clients = listOf(clientRepresentation)

            val userRepresentations = saleseazeConfig.defaultUsers.map {
                val clientRoles: MutableMap<String, List<String>> =
                    HashMap()
                clientRoles[
                        keycloakConfig.resource
                ] = determineRolesByUserName(it.key)

                val credentialRepresentation = CredentialRepresentation()
                credentialRepresentation.type =
                    CredentialRepresentation.PASSWORD
                credentialRepresentation.value = it.value

                // User
                val userRepresentation = UserRepresentation()
                userRepresentation.username = it.key
                userRepresentation.isEnabled = true
                userRepresentation.credentials =
                    listOf(credentialRepresentation)
                userRepresentation.clientRoles = clientRoles
                userRepresentation
            }.toList()
            realmRepresentation.users = userRepresentations
            keycloakAdmin.realms().create(realmRepresentation)

        } else {
            logger.info("Realm '{}' already configured", keycloakConfig.realm)
        }

        val admin = saleseazeConfig.defaultUsers.entries.first()
        logger.info("Testing getting token for '{}' ...", admin.key)

        val keycloakMovieApp =
            KeycloakBuilder.builder().serverUrl(keycloakConfig.authServerUrl)
                .realm(keycloakConfig.realm).username(admin.key)
                .password(admin.value)
                .clientId(keycloakConfig.resource).build()

        logger.info(
            "'{}' token: {}",
            admin.key,
            keycloakMovieApp.tokenManager().grantToken().token
        )
        logger.info(
            "'{}' initialization completed successfully!",
            keycloakConfig.realm
        )
    }

    fun determineRolesByUserName(userName: String) = when {
        userName.contains("super", true) -> ApplicationRoles
            .values().map { role -> role.name }
        userName.contains("admin", true) -> listOf(
            ApplicationRoles.SALESEAZE_MANAGER
                .name
        )
        userName.contains("user", true) -> listOf(
            ApplicationRoles.SALESEAZE_USER
                .name
        )
        else -> listOf(ApplicationRoles.SALESEAZE_USER.name)
    }
}




