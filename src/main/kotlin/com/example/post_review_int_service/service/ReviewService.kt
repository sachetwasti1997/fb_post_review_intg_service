package com.example.post_review_int_service.service

import com.example.post_review_int_service.custon_exception.PostClientException
import com.example.post_review_int_service.custon_exception.ReviewClientException
import com.example.post_review_int_service.dtos.Posts
import com.example.post_review_int_service.dtos.Reviews
import com.example.post_review_int_service.error_handler.ErrorResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Service
class ReviewService(
    val webClient: WebClient
) {

    @Value("\${review.baseUrl}")
    private val post_review_base_url : String ?= null

    @Value("\${review.scheme}")
    private val scheme: String ?= null

    @Value("\${review.host}")
    private val host: String ?= null

    @Value("\${review.port}")
    private val port: Int ?= null

    @Value("\${review.path}")
    private val path: String ?= null

    fun saveCommentToPost(reviews: Mono<Reviews>): Mono<Reviews> {
        return webClient
            .post()
            .uri("$post_review_base_url/create")
            .body(reviews, Reviews::class.java)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){ clientResponse ->
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(ReviewClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono(Reviews::class.java)
    }

    fun getAllCommentsForPost(
        postId:String,
        page:Long,
        size: Long)
    : Mono<List<Reviews>>{
        return webClient
            .get()
            .uri{
                it
                    .scheme(scheme)
                    .host(host)
                    .port(port!!)
                    .path("$path/comments/$postId")
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .build()
            }
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){ clientResponse ->
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND){
                    return@onStatus Mono.error(
                        ReviewClientException(
                            ErrorResponse(
                                "No Comments",
                                HttpStatus.NOT_FOUND
                            ).toString(),
                            clientResponse.statusCode()))
                }
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(ReviewClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono<List<Reviews>>()
            .log()
    }

    fun updateComment(
        commentId: String,
        reviews: Mono<Reviews>
    ):Mono<Reviews>{
        return webClient
            .put()
            .uri("${post_review_base_url}/$commentId")
            .body(reviews, Reviews::class.java)
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){clientResponse ->
                if (clientResponse.statusCode() == HttpStatus.NOT_FOUND){
                    return@onStatus Mono.error(
                        ReviewClientException(
                            ErrorResponse(
                                "Post not found",
                                HttpStatus.NOT_FOUND
                            ).toString(),
                            clientResponse.statusCode()))
                }
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(ReviewClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono(Reviews::class.java)
    }

    fun deleteComment(commentId: String):Mono<Void>{
        return webClient
            .delete()
            .uri("${post_review_base_url}/$commentId")
            .retrieve()
            .onStatus(HttpStatus::is4xxClientError){clientResponse ->
                clientResponse.bodyToMono(String::class.java)
                    .flatMap {
                        return@flatMap Mono.error(ReviewClientException(it, clientResponse.statusCode()))
                    }
            }
            .bodyToMono(Void::class.java)
    }

}


















