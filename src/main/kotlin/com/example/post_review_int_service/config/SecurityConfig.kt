package com.example.post_review_int_service.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain

@EnableWebFluxSecurity
class SecurityConfig @Autowired constructor(
    val authenticationManager: AuthenticationManager,
    val securityContext: SecurityContext
){

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun springSecurityFilterChain(httpSecurity: ServerHttpSecurity): SecurityWebFilterChain {
        httpSecurity
            .authorizeExchange()
            .pathMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
            .pathMatchers(HttpMethod.POST, "/api/v1/auth/subs").permitAll()
            .anyExchange().authenticated()
            .and()
            .httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContext)

        return httpSecurity.build()
    }

}