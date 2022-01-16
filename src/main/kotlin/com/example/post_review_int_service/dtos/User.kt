package com.example.post_review_int_service.dtos

import org.springframework.data.annotation.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class User(
    @Id
    var id: String ?= null,
    @field: NotNull(message = "Password cannot be null!")
    @field: NotEmpty(message = "")
    var password: String ?= null,
    var firstName: String ?= null,
    var lastName: String ?= null,
    var age : Int ?= null,
    @field: Email(message = "Email is not valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})\$")
    var email: String ?=null
) {
}