package com.saleseaze.api.config

import com.saleseaze.api.utils.ApplicationRoles
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver
import org.keycloak.adapters.springsecurity.KeycloakConfiguration
import org.keycloak.adapters.springsecurity.config.KeycloakWebSecurityConfigurerAdapter
import org.keycloak.adapters.springsecurity.management.HttpSessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy


@KeycloakConfiguration
class WebSecurityConfig : KeycloakWebSecurityConfigurerAdapter() {
    @Autowired
    fun configureGlobal(auth: AuthenticationManagerBuilder) {
        val keycloakAuthenticationProvider = keycloakAuthenticationProvider()
        keycloakAuthenticationProvider.setGrantedAuthoritiesMapper(
            SimpleAuthorityMapper()
        )
        auth.authenticationProvider(keycloakAuthenticationProvider)
    }

    @Bean
    fun keycloakConfigResolver(): KeycloakSpringBootConfigResolver {
        return KeycloakSpringBootConfigResolver()
    }

    @Bean
    override fun sessionAuthenticationStrategy(): SessionAuthenticationStrategy {
        return RegisterSessionAuthenticationStrategy(SessionRegistryImpl())
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        super.configure(http)
        http.authorizeRequests()
            .antMatchers(
                HttpMethod.GET,
                "/api/v1",
                "/api/v1/**",
                "/actuator/**"
            ).permitAll()
            .antMatchers("/api/v1/**/comments")
            .hasAnyRole(
                ApplicationRoles.SALESEAZE_MANAGER.name,
                ApplicationRoles.SALESEAZE_USER.name,
                ApplicationRoles.SALESEAZE_SUPER_ADMIN.name
            )
            .antMatchers("/api/v1", "/api/v1/**")
            .hasAnyRole(
                ApplicationRoles.SALESEAZE_MANAGER.name,
                ApplicationRoles.SALESEAZE_USER.name,
                ApplicationRoles.SALESEAZE_SUPER_ADMIN.name
            )
            .antMatchers(HttpMethod.GET, "/actuator/**").permitAll()
            .antMatchers(
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**"
            ).permitAll()
            .anyRequest().authenticated()
        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.cors().and().csrf().disable()
    }

    @Bean
    @ConditionalOnMissingBean(
        HttpSessionManager::class
    )
    override fun httpSessionManager(): HttpSessionManager {
        return HttpSessionManager()
    }

}