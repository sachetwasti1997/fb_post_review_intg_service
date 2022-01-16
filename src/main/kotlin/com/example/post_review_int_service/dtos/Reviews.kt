package com.example.post_review_int_service.dtos

import java.time.LocalDateTime

data class Reviews (
    val commentId: String ?= null,
    var postId: String ?= null,
    var comment: String ?= null,
    var dateCreated: LocalDateTime?= null
)