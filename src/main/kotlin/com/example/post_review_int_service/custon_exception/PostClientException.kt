package com.example.post_review_int_service.custon_exception

import org.springframework.http.HttpStatus

class PostClientException(
    var messageError: String ?= null,
    var httpStatus: HttpStatus ?= null
) : Exception(messageError) {
}