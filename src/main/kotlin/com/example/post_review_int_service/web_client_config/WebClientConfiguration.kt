package com.example.post_review_int_service.web_client_config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfiguration {

    @Bean
    fun webClient(builder: WebClient.Builder):WebClient{
        return builder.build()
    }

}