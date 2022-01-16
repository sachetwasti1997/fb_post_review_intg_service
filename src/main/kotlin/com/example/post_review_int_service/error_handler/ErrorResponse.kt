package com.example.post_review_int_service.error_handler

import org.springframework.http.HttpStatus

class ErrorResponse(
    var message: String,
    var httpStatus: HttpStatus
) {
    override fun toString(): String {
        return "{message='$message', httpStatus=$httpStatus}"
    }
}