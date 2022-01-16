package com.example.post_review_int_service.dtos

import javax.validation.constraints.NotNull

data class LoginRequestDto(
    @field: NotNull(message = "Email cannot be null!")
    val email: String,
    @field: NotNull(message = "Password cannot be null!")
    val password: String
)