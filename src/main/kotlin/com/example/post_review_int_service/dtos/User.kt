package com.example.post_review_int_service.dtos

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@Document(value = "users")
data class User(
    @Id
    var id: String ?= null,
    @field: NotNull(message = "Password cannot be null!")
    @field: NotEmpty(message = "Please enter a valid password!")
    var password: String ?= null,
    @field: NotNull(message = "First Name cannot be null!")
    @field: NotEmpty(message = "Please enter a valid firstName!")
    var firstName: String ?= null,
    @field: NotNull(message = "Last Name cannot be null!")
    @field: NotEmpty(message = "Please enter a valid lastName!")
    var lastName: String ?= null,
    @field: NotNull(message = "Age cannot be null!")
    @field: NotEmpty(message = "Please enter a valid age!")
    var age : Int ?= null,
    @field: Email(message = "Email is not valid", regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})\$")
    @field: NotNull(message = "Email cannot be null!")
    var email: String ?=null,
    var roles: List<String> ?= null
) {
}