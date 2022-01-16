package com.example.post_review_int_service.service

import com.example.post_review_int_service.custon_exception.PostClientException
import com.example.post_review_int_service.dtos.Posts
import com.example.post_review_int_service.error_handler.ErrorResponse
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

    @Value("\${post.scheme}")
    private val scheme: String ?= null

    @Value("\${post.host}")
    private val host: String ?= null

    @Value("\${post.port}")
    private val port: Int ?= null

    @Value("\${post.path}")
    private val path: String ?= null

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
                        .scheme(scheme)
                        .host(host)
                        .port(port!!)
                        .path("$path/$userId")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build()
            }
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){ clientResponse ->
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND){
                    return@onStatus Mono.error(
                        PostClientException(
                            ErrorResponse(
                        "No post found for the given user",
                                HttpStatus.NOT_FOUND
                        ).toString(),
                        clientResponse.statusCode()))
                }
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(PostClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono<List<Posts>>()
            .log()
    }

    fun updatePost(postId: String, posts: Mono<Posts>):Mono<Posts> {
        return webClient
            .put()
            .uri("${post_base_url}/$postId")
            .body(posts, Posts::class.java)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){clientResponse ->
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND){
                    return@onStatus Mono.error(
                        PostClientException(
                            ErrorResponse(
                                "Post not found",
                                HttpStatus.NOT_FOUND
                            ).toString(),
                            clientResponse.statusCode()))
                }
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(PostClientException(it, clientResponse.statusCode()))
                }
            }
            .bodyToMono(Posts::class.java)
    }

    fun deletePost(postId: String): Mono<Void>{
        return webClient
            .delete()
            .uri("${post_base_url}/$postId")
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){clientResponse ->
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(PostClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono(Void::class.java)
    }

}