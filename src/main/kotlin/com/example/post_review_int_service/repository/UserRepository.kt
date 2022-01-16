package com.example.post_review_int_service.repository

import com.example.post_review_int_service.dtos.User
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import reactor.core.publisher.Mono

interface UserRepository: ReactiveMongoRepository<User, String> {
    fun findByEmail(email: String):Mono<User>
}