package com.ecom.order.clients.restclient;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Configuration
public class RestClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder loadbalancedRestClientBuilder() {
        return RestClient.builder();
    }

    @Bean
    public RestClient userRestClient(RestClient.Builder builder) {
        String userServiceUrl = "http://user";
        return builder
                .baseUrl(userServiceUrl)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError
                        ,((request, response) -> Optional.empty()))
                .build();
    }

    @Bean
    public RestClient productRestClient(RestClient.Builder builder) {
        String productServiceUrl = "http://product";
        return builder
                .baseUrl(productServiceUrl)
                .defaultStatusHandler(HttpStatusCode::is4xxClientError
                        ,((request, response) -> Optional.empty()))
                .build();
    }
}
