package com.example.post_review_int_service.controller

import com.example.post_review_int_service.dtos.LoginRequestDto
import com.example.post_review_int_service.dtos.ResponseToken
import com.example.post_review_int_service.dtos.User
import com.example.post_review_int_service.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    val userService: UserService
) {
    @PostMapping("/login")
    fun userLogin(@RequestBody login:LoginRequestDto): Mono<ResponseEntity<ResponseToken>> {
        return userService.loginUser(login)
            .map{
                ResponseEntity<ResponseToken>(it, HttpStatus.OK)
            }
    }

    @PostMapping("/subs")
    fun subscribeUser(@RequestBody userDto: User): Mono<ResponseEntity<ResponseToken>> {
        return userService.saveUser(userDto)
            .map { result -> ResponseEntity<ResponseToken>(result, HttpStatus.OK) }
    }
}