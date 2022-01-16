package com.example.post_review_int_service.controller

import com.example.post_review_int_service.dtos.Posts
import com.example.post_review_int_service.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/post")
class PostController(
    val postService: PostService
) {

    @PostMapping("/save")
    fun savePost(@RequestBody posts: Mono<Posts>): Mono<ResponseEntity<Posts>>{
        return postService
            .savePost(posts)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

}