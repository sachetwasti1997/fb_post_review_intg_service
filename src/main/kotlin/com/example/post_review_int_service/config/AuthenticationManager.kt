package com.example.post_review_int_service.config

import com.example.post_review_int_service.repository.UserRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager @Autowired constructor(var jwtUtil: JwtUtil, var userRepository: UserRepository) :
    ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
        val token = authentication?.credentials.toString()
        return Mono.justOrEmpty(authentication)
            .flatMap { jwt -> mono {  validate(token) } }
            .onErrorMap { error -> IllegalArgumentException(error) }
    }

    private suspend fun validate(token:String): Authentication {
        val userName =jwtUtil.extractUserName(token)
        val user = userRepository.findByEmail(userName).awaitSingleOrNull()
        if (jwtUtil.validateToken(token, user)){
            val authorities : MutableList<SimpleGrantedAuthority> = mutableListOf()
            user?.roles?.forEach {
                val element = it
                authorities.add(SimpleGrantedAuthority(element))
            }
            return UsernamePasswordAuthenticationToken(user?.email, token, authorities)
        }
        throw IllegalArgumentException("Token is not valid!")
    }

}