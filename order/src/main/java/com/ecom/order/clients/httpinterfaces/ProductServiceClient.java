package com.ecom.order.clients.httpinterfaces;

import com.ecom.order.dtos.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface ProductServiceClient {


    @GetExchange("/api/products/{id}")
    ProductResponse getProductDetails(@PathVariable String id);


}
