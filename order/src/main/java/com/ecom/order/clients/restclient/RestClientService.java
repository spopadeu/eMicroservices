package com.ecom.order.clients.restclient;


import com.ecom.order.dtos.ProductResponse;
import com.ecom.order.dtos.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class RestClientService {

    private final RestClient userRestClient;
    private final RestClient productRestClient;

    public UserResponse getUser(String userId){
        return userRestClient
                .get()
                .uri("/api/users/" + userId)
                .retrieve()
                .body(UserResponse.class);
    }

    public ProductResponse getProduct(String productId){
        return productRestClient
                .get()
                .uri("/api/products/" + productId)
                .retrieve()
                .body(ProductResponse.class);
    }

}
