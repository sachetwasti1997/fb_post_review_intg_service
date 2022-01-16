package com.example.post_review_int_service.controller

import com.example.post_review_int_service.dtos.Posts
import com.example.post_review_int_service.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/{userId}")
    fun getPosts(
        @PathVariable userId: String,
        @RequestParam(value = "page", defaultValue = "0")page:Long,
        @RequestParam(value = "size", defaultValue = "3")size: Long
    ): Mono<ResponseEntity<List<Posts>>>{
        return postService
            .getPostForUser(userId, page, size)
            .map {
                ResponseEntity(it, HttpStatus.OK)
            }
            .log()
    }

}