package com.example.post_review_int_service.custon_exception

import org.springframework.http.HttpStatus

class ReviewClientException(
    private val messageError: String,
    val httpStatus: HttpStatus
): Exception(messageError) {
}