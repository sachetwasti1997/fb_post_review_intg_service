package com.example.post_review_int_service.error_handler

import com.example.post_review_int_service.custon_exception.PostClientException
import com.example.post_review_int_service.custon_exception.ReviewClientException
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class GlobalExceptionHandler: ErrorWebExceptionHandler {

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val dataFactory = exchange.response.bufferFactory()
        val errorMessage = dataFactory.wrap(ex.message!!.toByteArray())
        if (ex is PostClientException){
            exchange.response.statusCode = ex.httpStatus
            return exchange.response.writeWith (Mono.just(errorMessage))
        }else if (ex is ReviewClientException){
            exchange.response.statusCode = ex.httpStatus
            return exchange.response.writeWith (Mono.just(errorMessage))
        }
        exchange.response.statusCode = HttpStatus.INTERNAL_SERVER_ERROR
        return exchange.response.writeWith(Mono.just(errorMessage))
    }
}