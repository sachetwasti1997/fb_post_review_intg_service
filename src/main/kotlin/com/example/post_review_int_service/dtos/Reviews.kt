package com.example.post_review_int_service.dtos

import com.fasterxml.jackson.annotation.JsonFormat
import java.time.LocalDateTime

data class Reviews (
    val commentId: String ?= null,
    var postId: String ?= null,
    var comment: String ?= null,
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var dateCreated: LocalDateTime?= null
)