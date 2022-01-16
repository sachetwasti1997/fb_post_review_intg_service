package com.example.post_review_int_service.service

import com.example.post_review_int_service.config.JwtUtil
import com.example.post_review_int_service.dtos.LoginRequestDto
import com.example.post_review_int_service.dtos.ResponseToken
import com.example.post_review_int_service.dtos.User
import com.example.post_review_int_service.repository.UserRepository
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.nio.file.attribute.UserPrincipalNotFoundException

@Service
class UserService(
    val userRepository: UserRepository,
    val jwtUtil: JwtUtil,
    val bCryptPasswordEncoder: BCryptPasswordEncoder
) {

    fun loginUser(loginRequest: LoginRequestDto): Mono<ResponseToken> {
        return mono { loginSearch(loginRequest) }
    }

    fun saveUser(userDto: User): Mono<ResponseToken> {
        return mono { save(userDto) }
    }

    fun getByEmail(email: String): Mono<User> {
        return userRepository.findByEmail(email)
    }

    suspend fun save(userDto: User): ResponseToken{
        userDto.password = bCryptPasswordEncoder.encode(userDto.password)
        val user = userRepository.save(userDto).awaitSingleOrNull()
        return ResponseToken(jwtUtil.generateToken(user))
    }

    private suspend fun loginSearch(loginRequest: LoginRequestDto): ResponseToken{
        val user = loginRequest.email?.let { userRepository.findByEmail(it).awaitSingleOrNull() }
        if (user != null) {
            var passMatch = bCryptPasswordEncoder.matches(loginRequest.password, user.password)
            if (!passMatch){
                throw UserPrincipalNotFoundException("No User Found")
            }else{
                return ResponseToken(jwtUtil.generateToken(user))
            }
        }
        throw UserPrincipalNotFoundException("No User Found")
    }

}