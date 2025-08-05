package com.ecom.order.controller;


import com.ecom.order.dtos.OrderResponse;
import com.ecom.order.services.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-ID") Long userId
    ){
        Optional<OrderResponse> order = orderService.createOrder(userId);
        if(order.isPresent()){
            return new ResponseEntity<>(order.get(), HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
