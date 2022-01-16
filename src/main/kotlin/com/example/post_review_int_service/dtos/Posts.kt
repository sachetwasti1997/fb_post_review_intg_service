package com.example.post_review_int_service.dtos

data class Posts(
    val postId: String?= null,
    val userId: String ?= null,
    var title: String ?= null,
    var description: String ?= null,
);