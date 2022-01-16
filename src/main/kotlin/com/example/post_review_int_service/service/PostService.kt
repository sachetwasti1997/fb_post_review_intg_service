package com.example.post_review_int_service.service

import com.example.post_review_int_service.custon_exception.PostClientException
import com.example.post_review_int_service.dtos.Posts
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class PostService(
    val webClient: WebClient
){

    @Value("\${post.baseUrl}")
    private val post_base_url : String ?= null

    fun savePost(posts: Mono<Posts>):Mono<Posts>{
        return webClient
            .post()
            .uri("$post_base_url/save")
            .body(posts, Posts::class.java)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){ clientResponse ->
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(PostClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono(Posts::class.java)
    }

    fun getPostForUser(userId:String, page:Long, size: Long):Mono<List<Posts>>{
        return webClient
            .get()
            .uri{
                it
                    .scheme("http")
                    .host("localhost")
                    .port(8080)
                    .path("/api/v1/post/$userId")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .retrieve()
            .bodyToMono<List<Posts>>()
            .log()
    }

}