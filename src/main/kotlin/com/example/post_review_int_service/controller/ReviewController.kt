package com.example.post_review_int_service.controller

import com.example.post_review_int_service.dtos.Reviews
import com.example.post_review_int_service.service.ReviewService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/comment")
class ReviewController(
    val reviewService: ReviewService
) {

    @PostMapping("/save")
    fun createPostComment(@RequestBody reviews: Mono<Reviews>): Mono<ResponseEntity<Reviews>>{
        return reviewService
            .saveCommentToPost(reviews)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

    @GetMapping("/comments/{postId}")
    fun getAllCommentsForAPost(
        @PathVariable postId: String,
        @RequestParam(value = "page", defaultValue = "0")page: Long,
        @RequestParam(value = "size", defaultValue = "5")size: Long
    )
    :Mono<ResponseEntity<List<Reviews>>>{
        return reviewService
            .getAllCommentsForPost(postId, page, size)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

    @PutMapping("/{commentId}")
    fun updatePost(
        @PathVariable commentId:String,
        @RequestBody comments: Mono<Reviews>
    ):Mono<ResponseEntity<Reviews>>
    {
        return reviewService
            .updateComment(commentId, comments)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable commentId: String
    ):Mono<Void>
    {
        return reviewService
            .deleteComment(commentId)
    }
}